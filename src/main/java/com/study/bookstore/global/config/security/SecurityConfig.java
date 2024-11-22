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
        .authorizeHttpRequests(c -> c.requestMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().permitAll())
        .build();
  }


}
