package com.study.bookstore.domain.user.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

  private Long id;
  private String name;
  private String email;
  private String password;
  private String nickname;
  private String phoneNumber;
  private String address;
  private UserType userType;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
}