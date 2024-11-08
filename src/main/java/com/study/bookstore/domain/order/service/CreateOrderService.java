package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.PaymentMethod;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import com.study.bookstore.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateOrderService {

  private final OrderRepository orderRepository;

  public Long createOrder(PaymentMethod paymentMethod, User user) {
    try {
      Order order = Order.builder()
          .user(user)
          .paymentMethod(paymentMethod)
          .totalAmount(-1)
          .build();

      Order saveOrder = orderRepository.save(order);

      return saveOrder.getOrderId();
      // 저장한 order의 id값을 가져옴

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void updateTotalAmount(Long orderId, int totalAmount) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 주문이 존재하지 않습니다."));

    order.updateTotalAmount(totalAmount);

    orderRepository.save(order);
  }
}
