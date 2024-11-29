package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderService {

  private final OrderRepository orderRepository;

  public Order createOrder(Order order) {
      return orderRepository.save(order);
  }
}
