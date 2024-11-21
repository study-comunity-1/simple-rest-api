package com.study.bookstore.domain.review.facade;

import com.study.bookstore.domain.review.dto.req.CreateReviewReqDto;
import com.study.bookstore.domain.review.dto.req.UpdateReviewReqDto;
import com.study.bookstore.domain.review.dto.resp.AllReviewListRespDto;
import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.service.AllListReviewService;
import com.study.bookstore.domain.review.service.CreateReviewService;
import com.study.bookstore.domain.review.service.DeleteReviewService;
import com.study.bookstore.domain.review.service.ListReviewService;
import com.study.bookstore.domain.review.service.UpdateReviewService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.persistence.Column;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewFacade {

  private final CreateReviewService createReviewService;
  private final DeleteReviewService deleteReviewService;
  private final UpdateReviewService updateReviewService;
  private final ListReviewService listReviewService;
  private final AllListReviewService allListReviewService;

  //리뷰 추가
  public void createReview(CreateReviewReqDto req, User user){
    createReviewService.createReview(req, user);  // req와 user 전달
  }
  //리뷰 삭제
  public void deleteReview(Long reviewId, User user){
    deleteReviewService.deleteReview(reviewId, user);
  }
  //리뷰 수정
  public void updateReview(Long reviewId, UpdateReviewReqDto req, User user){
    updateReviewService.updateReview(reviewId, req, user);
  }
  //특정 책에 대한 리뷰 확인
  public List<ReviewListRespDto> getReviewsForBook(Long bookId){
    return listReviewService.getReviewsForBook(bookId);
  }
  //전체 리뷰 확인
  public Page<AllReviewListRespDto> getAllReviews(Pageable pageable){
   return allListReviewService.getAllReviews(pageable);
  }
}
