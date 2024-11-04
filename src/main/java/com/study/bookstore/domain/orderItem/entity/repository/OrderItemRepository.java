package com.study.bookstore.domain.orderItem.entity.repository;

import com.study.bookstore.domain.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
