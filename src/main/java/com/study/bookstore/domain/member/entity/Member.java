package com.study.bookstore.domain.member.entity;

import com.study.bookstore.domain.member.enums.AuthType;
import com.study.bookstore.domain.member.enums.Gender;
import com.study.bookstore.domain.member.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  private String password;
  private String phoneNumber;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @Enumerated(value = EnumType.STRING)
  private AuthType authType;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Role role;
}
