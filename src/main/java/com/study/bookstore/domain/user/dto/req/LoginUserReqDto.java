package com.study.bookstore.domain.user.dto.req;

import com.study.bookstore.domain.user.entity.User;

public record LoginUserReqDto(
    String email,
    String rawPassword
) {

  public User of(String password) {
    return User.builder()
        .email(this.email)
        .password(password)
        .build();
  }
}
