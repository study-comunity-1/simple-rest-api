package com.study.bookstore.domain.orderItem.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateOrderItemService {

  private final OrderRepository orderRepository;
  private final BookRepository bookRepository;
  private final OrderItemRepository orderItemRepository;

  public void createOrderItems(Long orderId, HttpSession session) {
    Order order = orderRepository.findById(orderId).orElse(null);

    if (order == null) {
      throw new NoSuchElementException("해당 ID의 주문이 존재하지 않습니다.");
    }

    Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
    if (cart == null) {
      throw new IllegalArgumentException("장바구니가 비어있습니다.");
    }

    List<OrderItem> orderItems = new ArrayList<>();

    for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
      Long bookId = entry.getKey();
      int quantity = entry.getValue();

      Book book = bookRepository.findById(bookId)
          .orElseThrow(() -> new NoSuchElementException("해당 ID의 책은 존재하지 않습니다."));

      OrderItem orderItem = OrderItem.builder()
          .order(order)
          .book(book)
          .quantity(quantity)
          .itemPrice(book.getPrice())
          .build();

      orderItemRepository.save(orderItem);
    }
  }
}
