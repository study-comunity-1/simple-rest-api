package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.order.dto.resp.GetOrderRespDto;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
import com.study.bookstore.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class GetOrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  public List<GetOrderRespDto> getOrder(Long orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 주문이 존재하지 않습니다."));

    if (!order.getUser().getUserId().equals(user.getUserId())) {
      throw new RuntimeException("권한이 없습니다.");
    }

    return orderItemRepository.findAllByOrder_orderId(orderId)
        .stream().map(GetOrderRespDto::from)
        .toList();


  }
}
