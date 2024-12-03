package com.study.bookstore.global.config.security;

import com.study.bookstore.global.jwt.filter.JwtFilter;
import com.study.bookstore.global.jwt.util.JwtUtil;
import com.study.bookstore.global.security.handler.CustomAccessDeniedHandler;
import com.study.bookstore.global.security.handler.CustomAuthenticationEntryPoint;
import com.study.bookstore.global.security.member.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtUtil jwtUtil;
  private final CustomAccessDeniedHandler AccessDeniedHandler;
  private final CustomAuthenticationEntryPoint AuthenticationEntryPoint;

  private static final String[] AUTH_WHITELIST = {
    "/api/v1/member/", "/swagger-ui/", "/api-docs", "/swagger-ui-custom.html",
    "/v3/api-docs/", "/api-docs/", "/swagger-ui.html", "/api/v1/auth/**", "/api/v1/member"
  };

//  @Bean
//  public BCryptPasswordEncoder bCryptPasswordEncoder() {
//    return new BCryptPasswordEncoder();
//  }

  @Bean
  public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .addFilterBefore(new JwtFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(c -> c.authenticationEntryPoint((AuthenticationEntryPoint))
            .accessDeniedHandler(AccessDeniedHandler))
        // .authorizeHttpRequests() : 이 부분에 url경로와 허용 권한을 지정
        .authorizeHttpRequests(c -> c
            .requestMatchers(AUTH_WHITELIST).permitAll()  // 인증이 필요없는 url => 모두 허용
            //.requestMatchers("/book/categories/{categoryId}").hasRole("ADMIN")  // 책 추가는 role이 ADMIN인 member만 가능
            .requestMatchers("/order/orders").hasAnyRole("ADMIN", "USER") // 책 주문은 role이 ADMIN, USER인 member만 가능
            //.anyRequest().authenticated())  // 그 외 요청은 로그인된 사용자만 접근 가능
            .anyRequest().permitAll())   // 그 외 요청 모두 접근 가능 (로그인을 하지 않아도 모든 요청을 허용 => 보안을 위해 하지 않는 것이 좋다)
        .build();
  }
}
