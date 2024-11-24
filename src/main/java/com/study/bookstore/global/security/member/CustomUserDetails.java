package com.study.bookstore.global.security.member;

import com.study.bookstore.domain.member.service.dto.CustomUserInfoDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
  private final CustomUserInfoDto member;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<String> roles = new ArrayList<>();
    roles.add("ROLE_" + member.role().toString());
    // member.role().toString() => String타입으로 USER, ADMIN 둘 중 하나
    // "ROLE_" + member.role().toString() =>
    //      member의 role에 따라서 ROLE_USER, ROLE_ADMIN이 됨

    return roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    // ROLE_USER, ROLE_ADMIN 같은 String타입들을 SimpleGrantedAuthority로 감싸서
    // Spring Security에서 권한을 관리할 수 있는 GrantedAuthority 형태로 변환시켜줌
  }

  @Override
  public String getPassword() {
    return member.password();
  }

  @Override
  public String getUsername() {
    return member.email();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
