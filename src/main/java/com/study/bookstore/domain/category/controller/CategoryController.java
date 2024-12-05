package com.study.bookstore.domain.category.controller;

import com.study.bookstore.domain.category.dto.req.CreateCategoryReqDto;
import com.study.bookstore.domain.category.dto.resp.GetAllCategoryListRespDto;
import com.study.bookstore.domain.category.dto.resp.GetCategoryListRespDto;
import com.study.bookstore.domain.category.facade.CategoryFacade;
import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.enums.Role;
import com.study.bookstore.domain.order.facade.OrderFacade;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.UserType;
import com.study.bookstore.global.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "카테고리 API")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryFacade categoryFacade;
  private final OrderFacade orderFacade;
  private final JwtUtil jwtUtil;


  @Operation(summary = "카테고리 추가")
  @PostMapping
  /*
public ResponseEntity<String> addCategory(@RequestBody CreateCategoryReqDto req,
      HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    UserType userType = user.getUserType();
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    try {
      categoryFacade.addCategory(req);
      return ResponseEntity.ok().body("카테고리  추가 완료");
    }catch (IllegalArgumentException e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
          .body(e.getMessage());
    }
  }
*/
  public ResponseEntity<String> addCategory(
      @RequestBody CreateCategoryReqDto req,
      HttpServletRequest request) {
    try {
      // 1. Authorization 헤더에서 JWT 토큰 추출
      String token = request.getHeader("Authorization");
      if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 실패: Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
      }

      String jwtToken = token.substring(7); // "Bearer " 이후의 토큰만 추출

      // 2. 토큰에서 이메일 추출
      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      // 3. 이메일로 사용자 정보 조회
      Member member = orderFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      // 4. 권한 확인
      Role role = member.getRole();
      if (role == null || role == role.USER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      }

      // 5. 카테고리 추가 로직 실행
      categoryFacade.addCategory(req);
      return ResponseEntity.ok().body("카테고리 추가 완료");
    } catch (IllegalArgumentException e) {
      // 예외 처리
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
          .body(e.getMessage());
    }
  }


  @Operation(summary = "카테고리 수정")
  @PutMapping("/{categoryId}")
  public ResponseEntity<String> updateCategory(@PathVariable Long categoryId,
     @RequestParam String categoryName, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    UserType userType = user.getUserType();
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    } try {
      categoryFacade.updateCategory(categoryName, categoryId);
      return ResponseEntity.ok().body("카테고리 수정 완료");
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
          .body(e.getMessage());
    }
  }
  @Operation(summary = "카테고리 삭제")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    UserType userType = user.getUserType();
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    } try {
      categoryFacade.deleteCategory(categoryId);
      return ResponseEntity.ok().body("카테고리 삭제 완료");
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
  @Operation(summary = "카테고리 상세 조회")
  @GetMapping("/{categoryId}")
  public ResponseEntity<?> getCategoryDetail(@PathVariable Long categoryId, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
    }
    UserType userType = user.getUserType();
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    try {
      GetCategoryListRespDto catetoryDetail = categoryFacade.getCategoryDetail(categoryId);
      return ResponseEntity.ok(catetoryDetail);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
  @Operation(summary = "카테고리 전체 목록 조회")
  @GetMapping("/all")
  public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, HttpSession session){
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
    }
    UserType userType = user.getUserType();
    if (userType == null || userType == UserType.USER){
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    try {
      //전체 카테고리 목록 가져오기
      GetAllCategoryListRespDto categoryList = categoryFacade.getCategories(page, size);
      //성공
      return ResponseEntity.ok(categoryList);
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}



