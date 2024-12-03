package com.study.bookstore.domain.orderItem.service;

import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadOrderItemService {

  private final OrderItemRepository orderItemRepository;

  public List<OrderItem> readOrderItemsByOrderId(Long orderId) {
    return orderItemRepository.findAllByOrder_orderId(orderId);
  }
}
