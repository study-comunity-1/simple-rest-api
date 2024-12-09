package com.study.bookstore.domain.review.service;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.review.dto.resp.AllReviewListRespDto;
import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AllListReviewService {

  private final ReviewRepository reviewRepository;

  public Page<AllReviewListRespDto> getAllReviews(Pageable pageable){

    // Page<Review>에서 Page<AllReviewListRespDto>로 변환
    return reviewRepository.findAll(pageable).map(review ->
        new AllReviewListRespDto(
            review.getReviewId(),
            review.getContent(),
            review.getRating(),
            review.getMember() != null ? review.getMember().getName() :  "Unknown",
            review.getCreatedDate(),
            review.getUpdatedDate()
        )
    );
  }
}
