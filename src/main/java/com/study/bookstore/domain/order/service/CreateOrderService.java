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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateOrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final BookRepository bookRepository;

  public void createOrder(PaymentMethod paymentMethod, HttpSession session, User user)
      throws Exception {
    try {
      Order order = Order.builder()
          .user(user)
          .paymentMethod(paymentMethod)
          .totalAmount(-1)
          .build();
/*
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
            .book(book)
            .quantity(quantity)
            .itemPrice(book.getPrice())
            .order(order)
            .build();

        orderItems.add(orderItem);
      }

      order.addOrderItem(orderItems);
*/
      orderRepository.save(order);
    } catch (Exception e) {
      throw new Exception("");
    }


  }
}
