package com.study.bookstore.domain.order.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import java.util.List;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findAllByStatus(Status status);

  // 특정 사용자가 특정 책을 주문한 적이 있는지 확인
  boolean existsByUserAndOrderItemsBook(User user, Book book);
}
