package com.study.bookstore.domain.review.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.review.dto.req.UpdateReviewReqDto;
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
public class UpdateReviewService {

  private final ReviewRepository reviewRepository;

  public void updateReview(Long reviewId, UpdateReviewReqDto req, User user) {

      //리뷰 아이디로 리뷰 존재 확인
      Optional<Review> optionalReview = reviewRepository.findById(reviewId);

      //리뷰가 존재하지 않을 때 예외
      if(optionalReview.isEmpty()){
        throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
      }
      //리뷰 객체 가져오기(리뷰 존재 확인 시에는 Optional이므로 객체를 아직 가져오지 않았기 때문)
      //Optional 객체에서 실제 Review 객체를 추출하기 위해서
      Review review = optionalReview.get();

      //리뷰 작성자와 현재 로그인한 사용자가 같은 지 확인
      if(!review.getUser().getUserId().equals(user.getUserId())){
        throw new IllegalArgumentException("리뷰 작성자만 수정이 가능합니다.");
      }
      //수정 하려는 평점 검증
      if (req.rating() < 0.0 || req.rating() > 5.0 || req.rating() % 0.5 != 0) {
      throw new IllegalArgumentException("평점은 0.0에서 5.0 사이의 값 이어야 하며 0.5 단위여야 합니다.");
    }
      //record 타입은 필드에 대해 자동으로 getter 메서드를 생성
      // req.rating()과 같은 메서드 호출이 가능
      review.setContent(req.content());
      review.setRating(req.rating());
      //변경된 리뷰 저장
      reviewRepository.save(review);
  }
}
