package com.study.bookstore.domain.user.dto.req;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.UserType;
import java.time.LocalDateTime;

public record CreateUserReqDto(
    String name,
    String email,
    String password,
    String nickname,
    String phoneNumber,
    String address,
    UserType userType
) {

  public User of(String password) {
    // Dto -> Entity로 바꿈
    return User.builder()
        .name(this.name)
        .email(this.email)
        .password(password)
        // 전달받은 암호화된 비밀번호를 password 필드에 넣음
        .nickname(this.nickname)
        .phoneNumber(this.phoneNumber)
        .address(this.address)
        .userType(this.userType)
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .build();
  }
}