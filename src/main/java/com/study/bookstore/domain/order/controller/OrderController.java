package com.study.bookstore.domain.order.controller;

import com.study.bookstore.domain.order.dto.resp.GetOrderListRespDto;
import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.service.CancelOrderService;
import com.study.bookstore.domain.order.service.CreateOrderService;
import com.study.bookstore.domain.order.service.GetOrderListService;
import com.study.bookstore.domain.order.service.GetOrderService;
import com.study.bookstore.domain.order.service.UpdateOrderStatusService;
import com.study.bookstore.domain.orderItem.service.CreateOrderItemService;
import com.study.bookstore.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final CreateOrderService createOrderService;
  private final CreateOrderItemService createOrderItemService;
  private final UpdateOrderStatusService updateOrderStatusService;
  private final CancelOrderService cancelOrderService;
  private final GetOrderListService getOrderListService;
  private final GetOrderService getOrderService;

  @Operation(summary = "주문 생성", description = "장바구니에 담긴 상품들을 주문합니다.")
  @PostMapping("/orders")
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

  @Operation(summary = "결제대기 -> 결제요청 상태 변경")
  @PutMapping("/readyForPayment")
  public ResponseEntity<String> updateStatus(@RequestParam Long orderId) {
    try {
      updateOrderStatusService.updateStatus(orderId);

      return ResponseEntity.ok().body("이제 결제가 가능합니다.");
    } catch (Exception e) {
      updateOrderStatusService.updateFail(orderId);
      // 예외시 상태를 결제실패로 변경
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "결제요청 -> 결제완료 상태 변경",
      description = "결제완료 이후의 상태는 일정시간이 지나면 자동으로 변경됩니다.")
  @PutMapping("/paymentCompleted")
  public ResponseEntity<String> updateToPaymentCompleted(@RequestParam Long orderId) {
    try {
      updateOrderStatusService.updateStatus(orderId);

      return ResponseEntity.ok().body("결제가 완료되었습니다.");
    } catch (Exception e) {
      // 만약 결제가 실패하거나 정해진 시간안에 결제를 하지 않은 경우에는 결제실패상태로 변경
      updateOrderStatusService.updateFail(orderId);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "주문취소", description = "유저의 요청으로 주문이 자동으로 취소됩니다.")
  @PutMapping("/cancel/{orderId}")
  public ResponseEntity<String> cancelOrder(@PathVariable Long orderId, HttpSession session) {
    try {
      User user = (User) session.getAttribute("user");

      if (user == null) {
        return ResponseEntity.badRequest().body("로그인 해주세요.");
      }

      cancelOrderService.cancelOrder(orderId, user);

      return ResponseEntity.ok().body("주문이 취소되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "주문 목록 조회", description = "유저의 주문 내역을 조회합니다.")
  @GetMapping("/list")
  public ResponseEntity<Object> gerOrderList(
      @RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "createdDate") String sortBy,
      @RequestParam(defaultValue = "DESC") String direction,
      HttpSession session) {
    try {
      User user = (User) session.getAttribute("user");

      if (user == null) {
        return ResponseEntity.badRequest().body("로그인 해주세요");
      }

      return ResponseEntity.ok().body(
          getOrderListService.getOrderList(
              pageNo, pageSize, sortBy, direction, user.getUserId()).getContent());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "주문 상세 조회", description = "해당 ID의 order를 상세조회합니다.")
  @GetMapping("/{orderId}")
  public ResponseEntity<Object> getOrder(@PathVariable Long orderId, HttpSession session) {
    try {
      User user = (User) session.getAttribute("user");

      if (user == null) {
        return ResponseEntity.badRequest().body("로그인 해주세요");
      }

      return ResponseEntity.ok().body(getOrderService.getOrder(orderId, user));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
