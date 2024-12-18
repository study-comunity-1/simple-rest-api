package com.study.bookstore.domain.user.facade;

import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.service.ReadReviewService;
import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.dto.req.LoginUserReqDto;
import com.study.bookstore.domain.user.dto.req.UpdateUserReqDto;
import com.study.bookstore.domain.user.dto.resp.UserReviewListRespDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.service.CreateUserService;
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
  private final BCryptPasswordEncoder passwordEncoder;
  private final ReadReviewService readReviewService;

  public void createUser(CreateUserReqDto req) {

    if (readUserService.readUserByEmail(req.email()) != null) {
      throw new IllegalArgumentException("이미 사용중인 email입니다.");
    }

    if (readUserService.readUserByNick(req.nick()) != null) {
      throw new IllegalArgumentException("이미 사용중인 nick입니다.");
    }

    if (readUserService.readUserByPhone(req.phone()) != null) {
      throw new IllegalArgumentException("이미 사용중인 phone입니다.");
    }

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
      user.softDelete();
      createUserService.createUser(user);
    }
  }

  public void updateUser(UpdateUserReqDto req, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      throw new RuntimeException("회원정보 수정은 로그인 후 가능합니다.");
    } else {
      User userUpdate = readUserService.readUserByUserId(user.getUserId());

      if (!user.getNick().equals(req.nick()) &&
          // 기존 닉네임과 동일한 경우에는 (수정하지 않은 경우) 아래 구문 수행하지 않음
          // => 닉네임을 수정했을 때만 중복체크를 한다.
          readUserService.readUserByNick(req.nick()) != null) {
        throw new IllegalArgumentException("이미 사용중인 nick입니다.");
      }

      userUpdate.updateUser(passwordEncoder.encode(req.password()), req.nick(), req.address());

      createUserService.createUser(userUpdate);
    }
  }


}
