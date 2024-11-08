package com.study.bookstore.domain.review.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  boolean existsByUserAndBook(User user, Book book);//리뷰 중복 체크
  Optional<Review> findByReviewIdAndUser(Long reviewId, User user); // 특정 유저가 특정 리뷰를 소유하는지 확인
  Optional<Review> findById(Long reviewId);//리뷰 아이디로 리뷰 찾기
}

