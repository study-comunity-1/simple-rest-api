package com.study.bookstore.domain.review.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadReviewService {
  private final BookRepository bookRepository;
  private final ReviewRepository reviewRepository;

  //특정 책 조회 및 검증
  public Book getBookWithReviews(Long bookId){
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

    // 책이 삭제된 상태인지 확인
    if (book.isDeleted()) {
      throw new RuntimeException("삭제된 책은 리뷰를 작성할 수 없습니다.");
    }
    return book;
  }

  //특정 리뷰 조회 및 검증
  public Review getReviewById(Long reviewId){
    Review review =  reviewRepository.findById(reviewId)
        .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
    //리뷰가 속한 책이 삭제되었는지 확인
    if(review.getBook().isDeleted()){
      throw new IllegalArgumentException("삭제된 책의 리뷰는 조회할 수 없습니다.");
    }
    return review;
  }
  //특정 책에 대한 리뷰 목록 조회
  public List<Review> findByBookId(Long bookId) {
// 책이 삭제되었는지 먼저 확인
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
    if (book.isDeleted()) {
      throw new RuntimeException("삭제된 책의 리뷰는 조회할 수 없습니다.");
    }
    // 책이 삭제되지 않았다면 리뷰 조회
    List<Review> reviews = reviewRepository.findByBookId(bookId);
    if (reviews.isEmpty()) {
      throw new RuntimeException("해당 책의 리뷰가 없습니다.");
    }
    return reviews;
  }
  // 전체 리뷰 조회
  public Page<Review> getAllReviews(Pageable pageable) {
    // ReviewRepository에서 모든 리뷰를 조회
    return reviewRepository.findAll(pageable);
  }

}
