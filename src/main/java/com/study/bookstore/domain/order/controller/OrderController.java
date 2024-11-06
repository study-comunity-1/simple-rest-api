package com.study.bookstore.domain.order.controller;

import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.service.CreateOrderService;
import com.study.bookstore.domain.order.service.UpdateOrderStatusService;
import com.study.bookstore.domain.orderItem.service.CreateOrderItemService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

      // return ResponseEntity.ok().body("주문이 완료되었습니다.");

      return ResponseEntity.status(HttpStatus.FOUND)
          // http 상태코드 300번대는 리다이렉트
          // HttpStatus.FOUND : http 상태코드 302
          // 근데 302는 getmapping으로 가는거라 지금은 putmapping으로 가려고해서 맞지 않음
          .header("Location", "/order/updateToReadyForPayment?orderId=" + orderId)
          // "Location" : 표준 HTTP 헤더. 이 헤더에 이동할 경로를 담는다.
          .build();
          // 응답이 완성되어 만들어지고, 클라이언트에게 전송된다.

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/updateToReadyForPayment")
  public ResponseEntity<?> updateStatus(@RequestParam Long orderId) {
    try {
      updateOrderStatusService.updateStatus(orderId);

      return ResponseEntity.ok().body("주문완");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
