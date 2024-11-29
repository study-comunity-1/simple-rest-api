package com.study.bookstore.domain.user.facade;

import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.service.ReadReviewService;
import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.dto.req.LoginUserReqDto;
import com.study.bookstore.domain.user.dto.req.UpdateUserReqDto;
import com.study.bookstore.domain.user.dto.resp.UserReviewListRespDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.service.CreateUserService;
import com.study.bookstore.domain.user.service.DeleteUserService;
import com.study.bookstore.domain.user.service.ReadUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class UserFacade {

  private final CreateUserService createUserService;
  private final ReadUserService readUserService;
  private final DeleteUserService deleteUserService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final ReadReviewService readReviewService;

  public void createUser(CreateUserReqDto req) {
    User user = req.of(passwordEncoder.encode(req.password()));
    // req.password() : 유저가 회원가입시 입력한 비밀번호를 가져옴 => 평문 비밀번호
    // passwordEncoder.encode(req.password()) : 가져온 평문 비밀번호를 암호화
    // req.of(passwordEncoder.encode(req.password())) : 암호화한 비밀번호를 of()메서드의 매개변수로 전달

    log.info("user_id: {}", user.getUserId());

    createUserService.createUser(user);
  }

  public void loginUser(LoginUserReqDto req, HttpSession session) {
    User byEmail = readUserService.readUserByEmail(req.email());
    // req.email() : 사용자가 로그인시 입력한 email 정보를 가져옴
    // userMapper.findByEmail(req.email()) : 해당 email과 일치하는 User를 가져옴
    // 만약 일치하는 정보가 없다면 byEmail == null

    if (byEmail == null || !passwordEncoder.matches(req.rawPassword(), byEmail.getPassword())) {
      // byEmail == null : 입력한 Email과 일치하는 User 계정이 없음
      // passwordEncoder.matches(req.rawPassword(), byEmail.getPassword())
      // : 입력한 password(평문)와 데이터베이스에 저장된 password(암호화)가 일치하는지 비교
      throw new RuntimeException("로그인 실패");
      // 일치하는 계정이 없는 경우 throw : 발생한 예외를 controller로 전가
    } else {
      session.setAttribute("user", byEmail);
      // 일치하는 계정이 있는 경우 : 세션에 해당 유저의 정보를 저장하여 로그인 상태를 유지함
    }
  }

  public void deleteUser(HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      throw new RuntimeException("회원탈퇴는 로그인 후 가능합니다.");
    } else {
      deleteUserService.deleteUser(user.getUserId());
    }
  }

  public void updateUser(UpdateUserReqDto req, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      throw new RuntimeException("회원정보 수정은 로그인 후 가능합니다.");
    } else {
      User userUpdate = readUserService.readUser(user.getUserId());

      userUpdate.updateUser(passwordEncoder.encode(req.password()), req.nick(), req.address());

      createUserService.createUser(userUpdate);
    }
  }

  public Page<UserReviewListRespDto> getUserReview(HttpSession session, Pageable pageable) {
    //1.유저를 조회한다.
    User user = (User) session.getAttribute("user");

    if(user == null){
      throw new IllegalArgumentException("로그인 해주세요.");
    }

    //2.유저가 작성한 리뷰 목록을 페이지네이션으로 조회한다.
    Page<Review> reviewPage = readReviewService.readReviewPage(user, pageable);

    //3.엔티티형식인 리뷰목록을 클라이언트에게 보여주기 위해 dto로 변환
    Page<UserReviewListRespDto> reviewListRespDtos = reviewPage.map(review ->
        new UserReviewListRespDto(
            review.getReviewId(),
            review.getRating(),  // rating은 이미 Double 타입이므로 캐스팅 필요 없음
            review.getContent(),
            review.getBook().getBookId(),
            review.getBook().getAuthor(),
            review.getCreatedDate()
        )
    );
    //4.dto를 포함한 응답을 반환한다.
    return reviewListRespDtos;
  }
}
