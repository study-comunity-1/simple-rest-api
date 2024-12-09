package com.study.bookstore.domain.member.service.dto;

import java.time.LocalDateTime;

public record MemberReviewListRespDto(
    Long reviewId,
    Double rating,
    String content,
    Long bookId,
    String author,
    LocalDateTime createdDate
) {

}
