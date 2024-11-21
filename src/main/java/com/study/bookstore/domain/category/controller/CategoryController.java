package com.study.bookstore.domain.category.controller;

import com.study.bookstore.domain.category.dto.req.CreateCategoryReqDto;
import com.study.bookstore.domain.category.facade.CategoryFacade;
import com.study.bookstore.domain.category.service.CreateCategoryService;
import com.study.bookstore.domain.category.service.DeleteCategoryService;
import com.study.bookstore.domain.category.service.UpdateCategoryService;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @Operation(summary = "카테고리 추가")
  @PostMapping
  public ResponseEntity<String> addCategory(@RequestBody CreateCategoryReqDto req,
      HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    UserType userType = user.getUserType();
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    } else {
      categoryFacade.addCategory(req);
      return ResponseEntity.ok().body("카테고리  추가 완료");
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
    } else {
      categoryFacade.updateCategory(categoryName, categoryId);
      return ResponseEntity.ok().body("카테고리 수정 완료");
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
    } else {
      categoryFacade.deleteCategory(categoryId);
      return ResponseEntity.ok().body("카테고리 삭제 완료");
    }
  }
}



