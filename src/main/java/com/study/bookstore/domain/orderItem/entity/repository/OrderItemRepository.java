package com.study.bookstore.domain.orderItem.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import java.util.List;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId AND oi.isDelete = false AND oi.order.isDelete = false")
  List<OrderItem> findAllByOrder_orderId(Long orderId);
}
