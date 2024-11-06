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

  private ReviewRepository reviewRepository;

  public void deleteReview(Long reviewId, User user){

    Optional<Review> reviewOptional = reviewRepository.findByReviewIdAndUser(reviewId, user);
    if(reviewOptional.isPresent()){
      reviewRepository.delete(reviewOptional.get());//Optional 안에 있는 Review 객체를 가져옴.
    }
    else{
      throw new IllegalArgumentException("리뷰 작성자만 삭제 가능합니다.");
    }


  }
}
