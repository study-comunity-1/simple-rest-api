package com.study.bookstore.domain.tokenBlacklist.service;

import com.study.bookstore.domain.tokenBlacklist.entity.TokenBlacklist;
import com.study.bookstore.domain.tokenBlacklist.entity.repository.TokenBlacklistRepository;
import com.study.bookstore.global.jwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

  private final TokenBlacklistRepository tokenBlacklistRepository;
  private final JwtUtil jwtUtil;

  public void createTokenBlacklist(String token, LocalDateTime expTime) {
  // 로그아웃 요청시 블랙리스트 테이블에 토큰을 추가하는 메서드

    TokenBlacklist tokenBlacklist = new TokenBlacklist(token, expTime);
    tokenBlacklistRepository.save(tokenBlacklist);
  }

  public boolean isBlacklisted(String token) {
  // 해당 토큰이 블랙리스트에 포함되어있는지 확인하는 메서드
  // 포함되어있다면 => true => 로그아웃 요청한 토큰
    return tokenBlacklistRepository.existsByTokenId(token);
  }
}
