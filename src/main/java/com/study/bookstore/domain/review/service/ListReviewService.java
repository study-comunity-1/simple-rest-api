package com.study.bookstore.domain.review.service;

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

   //책에 대한 리뷰 목록 조회
    public List<ReviewListRespDto> getReviewsForBook(Long bookId) {
      //책에 해당하는 review들을 db에서 조회
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
