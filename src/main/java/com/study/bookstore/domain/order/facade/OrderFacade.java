package com.study.bookstore.domain.order.facade;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.service.ReadBookService;
import com.study.bookstore.domain.order.dto.resp.GetOrderListRespDto;
import com.study.bookstore.domain.order.dto.resp.GetOrderRespDto;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.entity.Status;
import com.study.bookstore.domain.order.service.CreateOrderService;
import com.study.bookstore.domain.order.service.ReadOrderService;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.service.CreateOrderItemService;
import com.study.bookstore.domain.orderItem.service.ReadOrderItemService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class OrderFacade {

  private final CreateOrderService createOrderService;
  private final ReadOrderService readOrderService;
  private final CreateOrderItemService createOrderItemService;
  private final ReadBookService readBookService;
  private final ReadOrderItemService readOrderItemService;

  public Long createOrder(PaymentMethod paymentMethod, User user) {
    try {
      Order order = Order.builder()
          .user(user)
          .paymentMethod(paymentMethod)
          .totalAmount(-1)
          .build();

      Order saveOrder = createOrderService.createOrder(order);

      return saveOrder.getOrderId();
    } catch (IllegalArgumentException e) {
      log.error("** 입력값 검증 실패 : {} **", e.getMessage());
      throw new RuntimeException("주문 생성 중 오류가 발생하였습니다.", e);
    }
  }

  public void updateTotalAmount(Long orderId, int totalAmount) {
    try {
      Order order = readOrderService.readOrder(orderId);

      order.updateTotalAmount(totalAmount);

      createOrderService.createOrder(order);
    } catch (Exception e) {
      throw new RuntimeException("주문 총액 업테이트 중 오류가 발생하였습니다.", e);
    }
  }

  public int createOrderItems(Long orderId, HttpSession session) {
    try {
      Order order = readOrderService.readOrder(orderId);

      if (order == null) {
        throw new NoSuchElementException("존재하지 않는 주문입니다.");
      }

      Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
      if (cart == null) {
        throw new IllegalArgumentException("장바구니가 비어있습니다.");
      }

      int totalAmount = 0;

      for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
        Long bookId = entry.getKey();
        int quantity = entry.getValue();

        Book book = readBookService.readBook(bookId);

        OrderItem orderItem = OrderItem.builder()
            .order(order)
            .book(book)
            .quantity(quantity)
            .itemPrice(book.getPrice())
            .build();

        totalAmount += book.getPrice() * quantity;

        createOrderItemService.createOrderItems(orderItem);
      }

      return totalAmount;
    } catch (Exception e) {
      throw new RuntimeException("orderItems 생성 중 오류가 발생하였습니다.", e);
    }
  }

  public void updateStatus(Long orderId) {
    try {
      Order order = readOrderService.readOrder(orderId);

      if (order.getStatus() == Status.READY_FOR_PAYMENT) {
        // order의 현재 상태가 READY_FOR_PAYMENT(결제요청)상태인지 확인 -> 결제완료상태로 변경
        try {
          // 실제로 결제가 이루어지지는 않으므로 가상으로 결제가 완료되었다고 가정
          order.updateStatus(Status.PAYMENT_COMPLETED);
        } catch (Exception e) {
          throw new RuntimeException("결제 실패");
        }
      }

      if (order.getStatus() == Status.PENDING) {
        // order의 현재 상태가 PENDING(결제대기)상태인지 확인 -> 결제요청상태로 변경

        List<OrderItem> orderItems = readOrderItemService.readOrderItemsByOrderId(orderId);

        int totalAmount = 0;

        for (OrderItem orderItem : orderItems) {

          // 재고 확인
          Book book = orderItem.getBook();
          int quantity = orderItem.getQuantity();

          if (book.getStock() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
          }

          // 금액 확인
          totalAmount += book.getPrice() * quantity;
        }

        if (totalAmount != order.getTotalAmount()) {
          throw new IllegalStateException("금액이 일치하지 않습니다.");
        }

        // 재고, 금액 확인 후 주문이 가능하다면 '결제 대기' -> '결제 요청'으로 상태 변경
        order.updateStatus(Status.READY_FOR_PAYMENT);

        // 책 재고 --해주기
        for (OrderItem orderItem : orderItems) {
          orderItem.getBook().buyBook(orderItem.getQuantity());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("주문상태 업데이트 중 오류가 발생하였습니다.", e);
    }
  }

  public void updateFail(Long orderId) {
    try {
      Order order = readOrderService.readOrder(orderId);

      if (order.getStatus() == Status.PENDING || order.getStatus() == Status.READY_FOR_PAYMENT) {
        order.updateStatus(Status.PAYMENT_FAILED);
      }
    } catch (Exception e) {
      throw new RuntimeException("주문상태 업데이트 중 오류가 발생하였습니다.", e);
    }
  }

  public void cancelOrder(Long orderId, User user) {
    try {
      Order order = readOrderService.readOrder(orderId);

      if (!order.getUser().getUserId().equals(user.getUserId())) {
        throw new RuntimeException("해당 주문에 대한 권한이 없습니다.");
      }

      if (order.getStatus() == Status.CANCELLED) {
        throw new RuntimeException("이미 취소된 주무입니다.");
      }

      if (order.getStatus() == Status.PAYMENT_FAILED) {
        throw new RuntimeException("결제실패 처리된 주문입니다.");
      }

      if (order.getStatus() == Status.PREPARING_FOR_SHIPPING ||
          order.getStatus() == Status.SHIPPING ||
          order.getStatus() == Status.DELIVERED) {
        throw new RuntimeException("배송진행 이후에는 취소가 불가능합니다.");
      }

      if (order.getStatus() == Status.READY_FOR_PAYMENT ||
          order.getStatus() == Status.PAYMENT_COMPLETED) {


        List<OrderItem> orderItems = readOrderItemService.readOrderItemsByOrderId(orderId);

        for (OrderItem orderItem : orderItems) {
          orderItem.getBook().returnBook(orderItem.getQuantity());
        }
      }

      // 결제 대기상태에서는 아직 재고가 차감되지 않았으므로 상태만 변경하면 된다.

      order.updateStatus(Status.CANCELLED);
    } catch (Exception e) {
      throw new RuntimeException("주문 취소 중 오류가 발생하였습니다.", e);
    }
  }

  public Page<GetOrderListRespDto> getOrderList(
      int pageNo, int pageSize, String sortBy, String direction, Long userId) {
    try {
      Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));

      return readOrderService.readOrderList(userId, pageable);
    } catch (Exception e) {
      throw new RuntimeException("주문 목록 조회 중 오류가 발생하였습니다.", e);
    }
  }

  public List<GetOrderRespDto> getOrder(Long orderId, User user) {
    try {
      Order order = readOrderService.readOrder(orderId);

      if (!order.getUser().getUserId().equals(user.getUserId())) {
        throw new RuntimeException("권한이 없습니다.");
      }

      return readOrderItemService.readOrderItemsByOrderId(orderId)
          .stream().map(GetOrderRespDto::from)
          .toList();
    } catch (Exception e) {
      throw new RuntimeException("주문 상세 조회 중 오류가 발생하였습니다.", e);
    }
  }
}
