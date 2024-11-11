package com.study.bookstore.domain.order.dto.resp;

import com.study.bookstore.domain.orderItem.entity.OrderItem;
import lombok.Builder;

@Builder
public record GetOrderRespDto(
    Long orderId,
    Long bookId,
    int quantity,
    int itemPrice
) {

  public static GetOrderRespDto from(OrderItem orderItem) {
    return GetOrderRespDto.builder()
        .orderId(orderItem.getOrder().getOrderId())
        .bookId(orderItem.getBook().getBookId())
        .quantity(orderItem.getQuantity())
        .itemPrice(orderItem.getItemPrice())
        .build();
  }
}
