package com.study.bookstore.domain.user.dto.req;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.service.LoginUserService;

public record UpdateUserReqDto(
    String password,
    String nickname,
    String address
) {

  public User of(String password, Long id) {
    return User.builder()
        .password(password)
        .nickname(this.nickname)
        .address(this.address)
        .id(id)
        .build();
  }
}
