package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.order.dto.resp.GetOrderListRespDto;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadOrderService {

  private final OrderRepository orderRepository;

  public Order readOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
  }

  public Page<GetOrderListRespDto> readOrderList(Long userId, Pageable pageable) {

    return orderRepository.findAllByUser_userId(userId, pageable)
        .map(GetOrderListRespDto::from);
  }
}
