package com.study.bookstore.domain.review.dto.req;

public record UpdateReviewReqDto(
    Double rating,//새로운 평점
    String content//새로운 내용
) {}

