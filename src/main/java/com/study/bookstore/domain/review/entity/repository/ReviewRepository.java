package com.study.bookstore.domain.review.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.review.dto.resp.AllReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  boolean existsByUserAndBook(User user, Book book);//리뷰 중복 체크
  Optional<Review> findByReviewIdAndUser(Long reviewId, User user); // 특정 유저가 특정 리뷰를 소유하는지 확인
  Optional<Review> findById(Long reviewId);//리뷰 아이디로 리뷰 찾기
  @Query("SELECT r FROM Review r WHERE r.book.id = :bookId")
  List<Review> findByBookId(@Param("bookId") Long bookId);

  Page<Review> findByUser(User user, Pageable pageable); // 유저의 리뷰 목록을 페이지네이션으로 조회

}

