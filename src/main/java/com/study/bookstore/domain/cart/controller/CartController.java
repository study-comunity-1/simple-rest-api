package com.study.bookstore.domain.cart.controller;

import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.cart.dto.resp.GetCartListRespDto;
import com.study.bookstore.domain.cart.service.AddToCartService;
import com.study.bookstore.domain.cart.service.GetCartListService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

  private final AddToCartService addToCartService;
  private final GetCartListService getCartListService;

  @PostMapping
  public ResponseEntity<String> createCart(@RequestParam Long bookId, HttpSession session) {
    try {
      addToCartService.addToCart(bookId, session);
      return ResponseEntity.ok().body("장바구니에 추가되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/getCartList")
  public ResponseEntity<List<GetCartListRespDto>> getCartList(HttpSession session) {

    return ResponseEntity.ok().body(getCartListService.getCartList(session));
  }
}
