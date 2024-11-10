package com.study.bookstore.domain.review.dto.resp;

import java.time.LocalDateTime;

public record ReviewListRespDto(
    Long reviewId,
    String content,
    Double rating,
    String nick,
    LocalDateTime createdDate,
    LocalDateTime updatedDate
) {}//사용자에게 전달 될 데이터

