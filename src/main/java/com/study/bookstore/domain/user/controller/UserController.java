package com.study.bookstore.domain.user.controller;

import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.service.CreateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final CreateUserService createUserService;

  @Operation(summary = "유저생성", description = "입력한 정보로 유저를 생성합니다.(회원가입)")
  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody CreateUserReqDto req) {
    try {
      createUserService.createUser(req);
      return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }

  }
}
