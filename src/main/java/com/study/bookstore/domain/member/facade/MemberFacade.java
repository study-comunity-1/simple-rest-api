package com.study.bookstore.domain.member.facade;

import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.service.create.CreateMemberService;
import com.study.bookstore.domain.member.service.dto.CustomUserInfoDto;
import com.study.bookstore.domain.member.service.read.ReadMemberService;
import com.study.bookstore.global.jwt.util.JwtUtil;
import com.study.bookstore.web.api.member.dto.request.LoginMemberRequestDto;
import com.study.bookstore.web.api.member.dto.request.MemberCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberFacade {

  private final CreateMemberService createMemberService;
  private final ReadMemberService readMemberService;
  private final PasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  @Transactional
  public void createMember(MemberCreateRequestDto dto) {
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
}
