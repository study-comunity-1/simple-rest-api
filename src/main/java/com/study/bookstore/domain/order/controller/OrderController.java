package com.study.bookstore.domain.order.controller;

import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.service.CreateOrderService;
import com.study.bookstore.domain.order.service.UpdateOrderStatusService;
import com.study.bookstore.domain.orderItem.service.CreateOrderItemService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final CreateOrderService createOrderService;
  private final CreateOrderItemService createOrderItemService;
  private final UpdateOrderStatusService updateOrderStatusService;

  @PostMapping("/cartOrder")
  public ResponseEntity<String> createOrder(PaymentMethod paymentMethod, HttpSession session) {
    try {
      User user = (User) session.getAttribute("user");

      if (user == null) {
        return ResponseEntity.badRequest().body("로그인해주세요.");
      }

      Long orderId = createOrderService.createOrder(paymentMethod, user);
      // order 생성 후 생성된 orderId 반환

      int totalAmount = createOrderItemService.createOrderItems(orderId, session);
      // orderId와 장바구니를 이용해 orderItem 생성 후 총 금액 반환

      createOrderService.updateTotalAmount(orderId, totalAmount);
      // order에 totalAmount 값 넣기

      session.removeAttribute("cart");
      // 주문이 완료되면 장바구니 세션 삭제하기

      return ResponseEntity.ok().body("주문이 완료되었습니다.");
      // orderId를 가지고 @PutMapping("/updateToReadyForPayment")로 리다이렉트

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/updateToReadyForPayment")
  public ResponseEntity<String> updateStatus(@RequestParam Long orderId) {
    try {
      updateOrderStatusService.updateStatus(orderId);

      return ResponseEntity.ok().body("이제 결제가 가능합니다.");
    } catch (Exception e) {
      updateOrderStatusService.updateFail(orderId);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
