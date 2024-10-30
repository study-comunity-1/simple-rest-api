package com.study.bookstore.domain.user.dto.req;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.UserType;
import java.time.LocalDateTime;

public record CreateUserReqDto(
    String name,
    String email,
    String password,
    String nick,
    String phone,
    String address,
    UserType userType
) {
  private CreateUserReqDto
      (String name, String email, String password, String nick, String phone, String address) {
    this(name, email, password, nick, phone, address, UserType.USER);
  }

  public User of(String password) {
    // Dto -> Entity로 바꿈
    return User.builder()
        .name(this.name)
        .email(this.email)
        .password(password)
        // 전달받은 암호화된 비밀번호를 password 필드에 넣음
        .nick(this.nick)
        .phone(this.phone)
        .address(this.address)
        .userType(this.userType != null ? this.userType : UserType.USER)
        .build();
  }
}