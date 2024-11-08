package com.study.bookstore.domain.order.entity.repository;

import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findAllByStatus(Status status);
}
