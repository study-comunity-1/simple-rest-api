package com.study.bookstore.global.jwt.util;

import com.study.bookstore.domain.member.service.dto.CustomUserInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.SimpleTimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
  private final Key key;
  private final long accessTokenExpTime;

  public JwtUtil(
      @Value("${spring.jwt.secret.key}") String secretKey,
      @Value("${spring.jwt.expiration_time}") long accessTokenExpTime
  ) {
    byte[] decodeKey = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(decodeKey);
    this.accessTokenExpTime = accessTokenExpTime;
  }

  public String createAccessToken(CustomUserInfoDto member) {
    return createToken(member, accessTokenExpTime);
  }

  public String createToken(CustomUserInfoDto member, long expireTime) {
    Claims claims = Jwts.claims();
    claims.put("memberId", member.memberId());
    claims.put("email", member.email());
    claims.put("role", member.role());

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(tokenValidity.toInstant()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUserId(String token) {
    return parseClaims(token).get("email", String.class);
    // parseClaims(token) : token을 파싱하여 내용(payload)를 가져옴
    // .get("email", String.class) : payload중에서도 email에 해당하는 값을 String 타입으로 가져옴
  }

  public boolean validateToken(String token) {
  // jwt토큰을 검증하여 해당 token이 유효하면 true, 유효하지 않으면 false
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          // 서버가 가지고있는 secretKey를 이용해서 서명이 일치하는지 확인
          .build()
          .parseClaimsJws(token);

      return true;

    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
      // 유효하지 않은 경우
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
      // 만료된 경우
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
      // 지원하지 않는 형식인 경우
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
      // null이거나 빈문자열인 경우
    }

    return false;
  }

  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
