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
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);

      return true;

    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
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
