package com.study.bookstore.global.enums;

import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

public enum ResponseCode {
  LOGIN_SUCCESS("로그인 성공"),
  SIGNUP_SUCCESS("회원가입 성공"),
  GENERAL_ERROR("에러 발생");

  private final String message;

  ResponseCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
