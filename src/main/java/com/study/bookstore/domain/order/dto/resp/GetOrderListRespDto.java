package com.study.bookstore.domain.order.dto.resp;

import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetOrderListRespDto(
    Long orderId,
    Status status,
    LocalDateTime createdDate
) {

  public static GetOrderListRespDto from(Order order) {
    return GetOrderListRespDto.builder()
        .orderId(order.getOrderId())
        .status(order.getStatus())
        .createdDate(order.getCreatedDate())
        .build();
  }
}
