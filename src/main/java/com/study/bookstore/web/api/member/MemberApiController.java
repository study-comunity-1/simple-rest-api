package com.study.bookstore.web.api.member;

import com.study.bookstore.domain.member.facade.MemberFacade;
import com.study.bookstore.web.api.member.dto.request.LoginMemberRequestDto;
import com.study.bookstore.web.api.member.dto.request.MemberCreateRequestDto;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MEMBER API", description = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberApiController {

  private final MemberFacade memberFacade;

  @Operation(summary = "회원 신규 가입")
  @PostMapping("/member")
  public ResponseEntity<?> createMember(@Valid @RequestBody MemberCreateRequestDto dto) {
    memberFacade.createMember(dto);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<?> loginMember(@Valid @RequestBody LoginMemberRequestDto dto) {
    String token = memberFacade.login(dto);
    return ResponseEntity.status(HttpStatus.OK).body(token);
  }

  @Operation(summary = "로그아웃")
  @PostMapping("/logout")
  public ResponseEntity<?> logoutMember(@RequestHeader("Authorization") String authorization) {

    return ResponseEntity.ok().body("gdgd");
  }
}
