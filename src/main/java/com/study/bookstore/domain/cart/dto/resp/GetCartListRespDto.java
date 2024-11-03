package com.study.bookstore.domain.cart.dto.resp;

import lombok.Builder;

@Builder
public record GetCartListRespDto(
    String title,
    int quantity,
    int totalQuantity
) {
}
