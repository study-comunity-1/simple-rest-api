package com.study.bookstore.domain.review.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean existsReview(User user, Book book);//리뷰 중복 체크
}

