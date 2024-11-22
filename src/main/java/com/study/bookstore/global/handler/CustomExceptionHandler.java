package com.study.bookstore.global.handler;

import com.study.bookstore.global.enums.ResponseCode;
import com.study.bookstore.web.api.member.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Response<String>> handleAllExceptions(Exception ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new Response<String>(ResponseCode.GENERAL_ERROR, ex.getMessage()));
  }
}
