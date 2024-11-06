package com.study.bookstore.domain.review.dto.req;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.user.entity.User;

public record CreateReviewReqDto(

    Long bookId,
    Double rating,
    String content

) {

  public Review of(Book book, User user) {
    return Review.builder()
        .book(book)
        .user(user)
        .rating(this.rating)
        .content(this.content)
        .build();
  }
}
