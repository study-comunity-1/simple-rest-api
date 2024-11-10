package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.order.dto.resp.GetOrderListRespDto;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class GetOrderListService {

  private final OrderRepository orderRepository;

  public Page<?> getOrderList(
      int pageNo, int pageSize, String sortBy, String direction, Long userId) {
    Pageable pageable = PageRequest
        .of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));

    return orderRepository.findAllByUser_userId(userId, pageable)
        .map(GetOrderListRespDto::from);
  }
}
