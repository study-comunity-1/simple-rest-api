package com.study.bookstore.domain.member.facade;

import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.service.create.CreateMemberService;
import com.study.bookstore.domain.member.service.dto.CustomUserInfoDto;
import com.study.bookstore.domain.member.service.dto.MemberReviewListRespDto;
import com.study.bookstore.domain.member.service.read.ReadMemberService;
import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.service.ReadReviewService;
import com.study.bookstore.domain.user.dto.resp.UserReviewListRespDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.jwt.util.JwtUtil;
import com.study.bookstore.web.api.member.dto.request.LoginMemberRequestDto;
import com.study.bookstore.web.api.member.dto.request.MemberCreateRequestDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacade {

  private final CreateMemberService createMemberService;
  private final ReadMemberService readMemberService;
  private final PasswordEncoder encoder;
  private final JwtUtil jwtUtil;
  private final ReadReviewService readReviewService;

  @Transactional
  public void createMember(@Valid MemberCreateRequestDto dto) {
    try {
      Member member1 = readMemberService.findMemberByEmail(dto.email());

      if (member1 == null) {
        Member member = dto.of(encoder.encode(dto.password()));
        createMemberService.createMember(member);
      } else {
        throw new BadCredentialsException("존재하는 이메일 입니다.");
      }
    } catch (Exception e) {
      throw new BadCredentialsException("존재하는 이메일 입니다.(catch)");
    }
  }

  public String login(LoginMemberRequestDto dto) {
    Member member = readMemberService.findMemberByEmail(dto.email());

    if (member == null) {
      throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
    }

    if (!encoder.matches(dto.password(), member.getPassword())) {
      throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
    }

    CustomUserInfoDto customUserInfoDto = CustomUserInfoDto.of(member);
    return jwtUtil.createAccessToken(customUserInfoDto);
  }
  public Page<MemberReviewListRespDto> getMemberReview(LoginMemberRequestDto dto, Pageable pageable) {
    //1.유저를 조회한다.
    Member member = readMemberService.findMemberByEmail(dto.email());

    if(member == null){
      throw new IllegalArgumentException("로그인 해주세요.");
    }

    //2.유저가 작성한 리뷰 목록을 페이지네이션으로 조회한다.
    Page<Review> reviewPage = readReviewService.readReviewPage(member, pageable);

    //3.엔티티형식인 리뷰목록을 클라이언트에게 보여주기 위해 dto로 변환
    Page<MemberReviewListRespDto> reviewListRespDtos = reviewPage.map(review ->
        new MemberReviewListRespDto(
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
