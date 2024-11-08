package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import com.study.bookstore.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CancelOrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  public void cancelOrder(Long orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 주문이 존재하지 않습니다."));

    if (!order.getUser().getUserId().equals(user.getUserId())) {
      throw new RuntimeException("해당 주문에 대한 권한이 없습니다.");
    }

    if (order.getStatus() == Status.CANCELLED) {
      throw new RuntimeException("이미 취소된 주문입니다.");
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


      List<OrderItem> orderItems = orderItemRepository.findAllByOrder_orderId(orderId);

      for (OrderItem orderItem : orderItems) {
        orderItem.getBook().returnBook(orderItem.getQuantity());
      }
    }

    // 결제 대기상태에서는 아직 재고가 차감되지 않았으므로 상태만 변경하면 된다

    order.updateStatus(Status.CANCELLED);
  }
}
