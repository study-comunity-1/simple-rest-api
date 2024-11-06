package com.study.bookstore.domain.order.controller;

import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.service.CreateOrderService;
import com.study.bookstore.domain.orderItem.service.CreateOrderItemService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final CreateOrderService createOrderService;
  private final CreateOrderItemService createOrderItemService;

  @PostMapping("/cartOrder")
  public ResponseEntity<?> createOrder(PaymentMethod paymentMethod, HttpSession session) {
    try {
      User user = (User) session.getAttribute("user");

      if (user == null) {
        return ResponseEntity.badRequest().body("로그인해주세요.");
      }

      Long orderId = createOrderService.createOrder(paymentMethod, session, user);

      createOrderItemService.createOrderItems(orderId, session);

      session.removeAttribute("cart");

      return ResponseEntity.ok().body("주문이 완료되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("??");
    }
  }
}
