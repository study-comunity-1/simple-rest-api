package com.study.bookstore.global.jwt.filter;

import com.study.bookstore.global.jwt.util.JwtUtil;
import com.study.bookstore.global.security.member.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    // http 요청 헤더에서 "Authorization"값만 가져옴

    if (authorization != null && authorization.startsWith("Bearer ")) {
                                // 보통 jwt 토큰은 "Bearer "으로 시작한다
      String token = authorization.substring(7);
      // "Bearer "이후의 실제 토큰부분만 token에 담음

      if (jwtUtil.validateToken(token)) {
      // jwt 토큰이 유효한지 검증
        String userId = jwtUtil.getUserId(token);
        // jwt 토큰에 담긴 내용 중 email을 userId 변수에 담음

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        // email을 이용해 db에 일치하는 유저가 있는지 확인하고, 있다면 해당 유저의 정보를 userDetails에 담음

        if (userDetails != null) {
        // userDetails가 null이 아니라는 의미 => 유효한 사용자정보가 담겨있다는 의미
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          // userDetails에 담긴 사용자 정보와, userDetails.getAuthorities()인 권한
          // 이 두개를 가지고 해당 사용자가 인증된 사용자인지 확인할 수 있는 토큰

          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
