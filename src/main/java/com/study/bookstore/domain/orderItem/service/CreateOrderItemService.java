package com.study.bookstore.domain.orderItem.service;

import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderItemService {

  private final OrderItemRepository orderItemRepository;

  public void createOrderItems(OrderItem orderItem) {
    orderItemRepository.save(orderItem);
  }
}
