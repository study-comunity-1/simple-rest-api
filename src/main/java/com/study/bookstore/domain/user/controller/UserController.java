package com.study.bookstore.domain.user.controller;

import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.dto.req.LoginUserReqDto;
import com.study.bookstore.domain.user.dto.req.UpdateUserReqDto;
import com.study.bookstore.domain.user.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @Operation(summary = "유저생성", description = "입력한 정보로 유저를 생성합니다.(회원가입)")
  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody CreateUserReqDto req) {
    try {
      userFacade.createUser(req);
      return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "로그인", description = "사용자가 입력한 email, password로 로그인")
  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestBody LoginUserReqDto req, HttpSession session) {
    try {
      userFacade.loginUser(req, session);
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
  public ResponseEntity<String> logoutUser(HttpSession session) {
    session.removeAttribute("user");
    /*
    로그아웃이 성공했는지 확인하기위한 코드 => user == null : 세선에 저장되어있는 정보가 없다는 의미
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.ok().body("세션에 저장되어있는 user 정보가 없습니다.");
    }
    */

    session.removeAttribute("cart");
    // 로그아웃시 세션에서 장바구니 정보도 삭제

    return ResponseEntity.ok().body("로그아웃되었습니다.");
  }

  @Operation(summary = "유저삭제", description = "로그인되어있는 유저의 정보를 삭제합니다.(회원탈퇴)")
  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser(HttpSession session) {
    try {
      userFacade.deleteUser(session);
      session.removeAttribute("user");
      session.removeAttribute("cart");
      return ResponseEntity.ok().body("회원탈퇴한 계정입니다.");
    } catch (Exception e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @Operation(summary = "유저정보수정", description = "유저의 회원정보를 수정합니다.")
  @PutMapping("/update")
  public ResponseEntity<String> updateUser(@RequestBody UpdateUserReqDto req, HttpSession session) {
    try {
      userFacade.updateUser(req, session);
      session.removeAttribute("user");
      session.removeAttribute("cart");
      return ResponseEntity.ok().body("회원 정보 수정이 완료되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @Operation(summary = "유저 리뷰 리스트 확인", description = "유저가 쓴 리뷰 목록 조회")
  @GetMapping("/{userId}/reviews")
  public ResponseEntity<?> getUserReviews(
      HttpSession session,
      @RequestParam(defaultValue = "1") int page, // 기본값을 1로 설정
      @RequestParam(defaultValue = "10") int size  // 기본값을 10으로 설정
  ) {
    Pageable pageable = PageRequest.of(page - 1, size); // Spring은 0부터 시작하므로 page - 1
    return ResponseEntity.ok().body(userFacade.getUserReview(session, pageable).getContent());
  }
}