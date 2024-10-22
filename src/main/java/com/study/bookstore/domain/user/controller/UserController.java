package com.study.bookstore.domain.user.controller;

import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.dto.req.LoginUserReqDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.service.CreateUserService;
import com.study.bookstore.domain.user.service.LoginUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
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
  private final LoginUserService loginUserService;

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

  @Operation(summary = "로그인", description = "사용자가 입력한 email, password로 로그인")
  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginUserReqDto req, HttpSession session) {
    try {
      loginUserService.loginUser(req, session);
      /*
      로그인이 성공했는지 확인하기위한 코드 => 해당코드 실행시 Response body에 로그인된 계정 정보가 출력된다.
      User user = (User) session.getAttribute("user");
      return ResponseEntity.ok(user);
      */
      return ResponseEntity.ok().body("로그인에 성공하였습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(401).body("이메일 혹은 비밀번호가 일치하지않습니다.");
      // 일치하는 계정이 없는 경우 service에서 controller로 예외 전가
    }
  }

  @Operation(summary = "로그아웃", description = "세션에 저장되어있는 유저의 정보를 삭제합니다.")
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(HttpSession session) {
    session.removeAttribute("user");
    /*
    로그아웃이 성공했는지 확인하기위한 코드 => user == null : 세선에 저장되어있는 정보가 없다는 의미
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.ok().body("세션에 저장되어있는 user 정보가 없습니다.");
    }
    */
    return ResponseEntity.ok().body("로그아웃되었습니다.");
  }
}
