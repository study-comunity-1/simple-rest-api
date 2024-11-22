package com.study.bookstore.web.api.member.dto.response;

import com.study.bookstore.global.enums.ResponseCode;

public class Response<T> {
  private ResponseCode code;
  private T data;

  public Response(ResponseCode responseCode, T message) {
    this.code = responseCode;
    this.data = message;
  }

}
