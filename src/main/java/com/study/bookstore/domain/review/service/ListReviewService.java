package com.study.bookstore.domain.review.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ListReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

   //책에 대한 리뷰 목록 조회
    public List<ReviewListRespDto> getReviewsForBook(Long bookId) {
      // bookId로 Book 객체를 찾기
      Book book = bookRepository.findById(bookId)
          .orElseThrow(() -> new RuntimeException("Book not found"));

      // Book 객체를 사용하여 리뷰 조회
      List<Review> reviews = reviewRepository.findByBookId(bookId);

      // Review 객체들을 ReviewListRespDto로 변환(엔티티 그대로 보내주면 보안상 문제)
      return reviews.stream()
          .map(review -> new ReviewListRespDto(
              review.getReviewId(),
              review.getContent(),
              review.getRating(),
              review.getUser().getNick(),
              review.getCreatedDate(),
              review.getUpdatedDate()
          ))
          .collect(Collectors.toList());
    }
}
