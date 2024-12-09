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

@Service
@RequiredArgsConstructor
public class ListReviewService {

  private final ReviewRepository reviewRepository;
  private final BookRepository bookRepository;

  //책에 대한 리뷰 목록 조회
  public List<ReviewListRespDto> getReviewsForBook(List<Review> reviews) {

    return reviews.stream()
        .map(review -> new ReviewListRespDto(
            review.getReviewId(),
            review.getContent(),
            review.getRating(),
            review.getMember().getName(),
            review.getCreatedDate(),
            review.getUpdatedDate()
        ))
        .collect(Collectors.toList());
  }
}
