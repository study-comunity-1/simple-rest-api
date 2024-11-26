package com.study.bookstore.web.api.member;

import com.study.bookstore.domain.member.facade.MemberFacade;
import com.study.bookstore.domain.tokenBlacklist.service.TokenBlacklistService;
import com.study.bookstore.global.jwt.util.JwtUtil;
import com.study.bookstore.global.security.member.CustomUserDetails;
import com.study.bookstore.web.api.member.dto.request.LoginMemberRequestDto;
import com.study.bookstore.web.api.member.dto.request.MemberCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "MEMBER API", description = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberApiController {

  private final MemberFacade memberFacade;
  private final JwtUtil jwtUtil;
  private final TokenBlacklistService tokenBlacklistService;

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

  @Operation(summary = "로그아웃", security = @SecurityRequirement(name = "bearerAuth"))
  @PostMapping("/logout")
  public ResponseEntity<?> logoutMember(Authentication authentication) {
    if (authentication == null) {
      return ResponseEntity.badRequest().body("인증 실패");
    }

    log.info("** authentication : {} **", authentication);

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    log.info("** userDetails : {} **", userDetails);

    String jti = userDetails.getJti();
    /*
    String name = authentication.getName();
    log.info("** 로그아웃 요청한 사용자 : {} **", name);

    // authentication에서 jwt 토큰을 추출
    String jwtToken = jwtUtil.getJwtFromAuth(authentication);
    if (jwtToken == null) {
      return ResponseEntity.badRequest().body("jwtToken Null");
    }
    if (!jwtUtil.validateToken(jwtToken)) {
      return ResponseEntity.badRequest().body("유효하지 않은 토큰");
    }

    String jti = jwtUtil.parseClaims(jwtToken).getId();
    LocalDateTime expTime = jwtUtil.parseClaims(jwtToken)
        .getExpiration()
        .toInstant()
        .atZone(ZonedDateTime.now().getZone())
        .toLocalDateTime();
    */

    //tokenBlacklistService.createTokenBlacklist(jti, expTime);

    return ResponseEntity.ok().body("로그아웃 성공");
  }
}
