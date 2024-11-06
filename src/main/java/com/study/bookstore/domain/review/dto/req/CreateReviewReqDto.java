package com.study.bookstore.domain.review.dto.req;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.user.entity.User;

public record CreateReviewReqDto(

    Long bookId,// 컨트롤러로부터 데이터를 받을 때, 특정 책에 대한 리뷰임을 식별하기 위함
    Double rating,
    String content

) {
  // Book과 User 엔티티를 받아서 Review로 변환하는 메서드
  public Review of(Book book, User user) {
    return Review.builder()
        .book(book)
        .user(user)
        .rating(this.rating)
        .content(this.content)
        .build();
  }
}
