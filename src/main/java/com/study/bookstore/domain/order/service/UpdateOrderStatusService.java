package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateOrderStatusService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  public void updateStatus(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 주문이 존재하지 않습니다."));

    if (order.getStatus() == Status.PENDING) {
    // order의 현재 상태가 PENDING(결제대기)상태인지 확인 -> 결제요청상태로 변경

      List<OrderItem> orderItems = orderItemRepository.findAllByOrder_orderId(orderId);

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

    if (order.getStatus() == Status.READY_FOR_PAYMENT) {
    // order의 현재 상태가 READY_FOR_PAYMENT(결제요청)상태인지 확인 -> 결제완료상태로 변경
      try {
        // 실제로 결제가 이루어지지는 않으므로 가상으로 결제가 완료되었다고 가정
        order.updateStatus(Status.PAYMENT_COMPLETED);
      } catch (Exception e) {
        throw new RuntimeException("결제 실패");
      }

    }
  }

  public void updateFail(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 주문이 존재하지 않습니다."));

    if (order.getStatus() == Status.PENDING || order.getStatus() == Status.READY_FOR_PAYMENT) {
      order.updateStatus(Status.PAYMENT_FAILED);
    }
  }
}
