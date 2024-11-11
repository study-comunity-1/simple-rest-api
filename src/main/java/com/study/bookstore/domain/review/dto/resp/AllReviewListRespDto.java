package com.study.bookstore.domain.review.dto.resp;

import java.time.LocalDateTime;

public record AllReviewListRespDto(
    Long reviewId,
    String content,
    Double rating,
    String nick,
    LocalDateTime createdDate,
    LocalDateTime updatedDate
) {
}