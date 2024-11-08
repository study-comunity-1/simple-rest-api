package com.study.bookstore.domain.review.service;

import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import com.study.bookstore.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DeleteReviewService {

  private final ReviewRepository reviewRepository;

  public void deleteReview(Long reviewId, User user){
    //리뷰가 존재하는 지 확인
    Optional<Review> optionalReview = reviewRepository.findById(reviewId);
    if(optionalReview.isEmpty()){
      throw new IllegalArgumentException("삭제 할 리뷰가 존재하지 않습니다.");
    }
    //옵셔널 리뷰에서 리뷰 객체를 가져옴
    Review review = optionalReview.get();
    //리뷰 작성자와 사용자가 같은 지 확인
    if(!review.getUser().getUserId().equals(user.getUserId())){
      throw new IllegalArgumentException("리뷰 작성자만 삭제 가능합니다.");
    }
    reviewRepository.delete(review);
  }
}
