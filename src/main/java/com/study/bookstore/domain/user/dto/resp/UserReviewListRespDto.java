package com.study.bookstore.domain.user.dto.resp;

import java.time.LocalDateTime;

public record UserReviewListRespDto(
    Long reviewId,
    Double rating,
    String content,
    Long bookId,
    String author,
    LocalDateTime createdDate
) {
}
