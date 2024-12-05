package com.study.bookstore.domain.order.controller;

import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.facade.OrderFacade;
import com.study.bookstore.domain.order.service.ReadOrderService;
import com.study.bookstore.domain.tokenBlacklist.service.TokenBlacklistService;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderFacade orderFacade;
  private final JwtUtil jwtUtil;
  private final TokenBlacklistService tokenBlacklistService;
  private final ReadOrderService readOrderService;

  @Operation(summary = "주문 생성", description = "장바구니에 담긴 상품들을 주문합니다.")
  @PostMapping("/orders")
  public ResponseEntity<?> createOrder(PaymentMethod paymentMethod, HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization");

      if (token == null) {
        return ResponseEntity.badRequest().body("인증 정보가 없습니다. 로그인 해주세요.");
      }

      if (!token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 형식이 잘못되었습니다. 유효한 토큰을 제시해주세요");
      }

      String jwtToken = token.substring(7);

      if (tokenBlacklistService.isBlacklisted(jwtToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃된 계정입니다.");
      }

      String email = jwtUtil.getUserId(jwtToken);

      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      Member member = orderFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      HttpSession session = request.getSession();

      Long orderId = orderFacade.createOrder(paymentMethod, member);
      // order 생성 후 생성된 orderId 반환

      int totalAmount = orderFacade.createOrderItems(orderId, session);
      // orderId와 장바구니를 이용해 orderItem 생성 후 총 금액 반환

      orderFacade.updateTotalAmount(orderId, totalAmount);
      // order에 totalAmount 값 넣기

      session.removeAttribute("cart");
      // 주문이 완료되면 장바구니 세션 삭제하기

      return ResponseEntity.ok().body("주문이 완료되었습니다.");
      // orderId를 가지고 @PutMapping("/readyForPayment")로 리다이렉트

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "결제대기 -> 결제요청 상태 변경")
  @PutMapping("/readyForPayment")
  public ResponseEntity<String> updateStatus(
      @RequestParam Long orderId, HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization");

      if (token == null) {
        return ResponseEntity.badRequest().body("인증 정보가 없습니다. 로그인 해주세요.");
      }

      if (!token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 형식이 잘못되었습니다. 유효한 토큰을 제시해주세요");
      }

      String jwtToken = token.substring(7);

      if (tokenBlacklistService.isBlacklisted(jwtToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃된 계정입니다.");
      }

      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      Order order = readOrderService.readOrder(orderId);
      if (order == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 주문이 존재하지 않습니다.");
      }
      log.info("** orderId : {} **", order.getOrderId());
      log.info("** orderEmail : {} **", order.getMember().getEmail());

      if (!order.getMember().getEmail().equals(email)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 주문에 권한이 없습니다.");
      }

      orderFacade.updateStateToReadyForPayment(orderId);

      return ResponseEntity.ok().body("이제 결제가 가능합니다.");
    } catch (Exception e) {
      orderFacade.updateFail(orderId);
      // 예외시 상태를 결제실패로 변경
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "결제요청 -> 결제완료 상태 변경",
      description = "결제완료 이후의 상태는 일정시간이 지나면 자동으로 변경됩니다.")
  @PutMapping("/paymentCompleted")
  public ResponseEntity<String> updateToPaymentCompleted(
      @RequestParam Long orderId, HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization");

      if (token == null) {
        return ResponseEntity.badRequest().body("인증 정보가 없습니다. 로그인 해주세요.");
      }

      if (!token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 형식이 잘못되었습니다. 유효한 토큰을 제시해주세요");
      }

      String jwtToken = token.substring(7);

      if (tokenBlacklistService.isBlacklisted(jwtToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃된 계정입니다.");
      }

      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      Order order = readOrderService.readOrder(orderId);
      if (order == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 주문이 존재하지 않습니다.");
      }

      if (!order.getMember().getEmail().equals(email)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 주문에 권한이 없습니다.");
      }

      orderFacade.updateStateToPaymentCompleted(orderId);

      return ResponseEntity.ok().body("결제가 완료되었습니다.");
    } catch (Exception e) {
      // 만약 결제가 실패하거나 정해진 시간안에 결제를 하지 않은 경우에는 결제실패상태로 변경
      orderFacade.updateFail(orderId);
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

      orderFacade.cancelOrder(orderId, user);

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
          orderFacade.getOrderList(
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

      return ResponseEntity.ok().body(orderFacade.getOrder(orderId, user));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
