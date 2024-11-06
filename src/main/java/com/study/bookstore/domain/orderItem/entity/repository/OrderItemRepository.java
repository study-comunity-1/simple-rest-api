package com.study.bookstore.domain.orderItem.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

boolean existsByUserAndBook(User user, Book book);
}
