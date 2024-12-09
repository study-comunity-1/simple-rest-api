package com.study.bookstore.domain.review.facade;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.service.read.ReadMemberService;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.review.dto.req.CreateReviewReqDto;
import com.study.bookstore.domain.review.dto.req.UpdateReviewReqDto;
import com.study.bookstore.domain.review.dto.resp.AllReviewListRespDto;
import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import com.study.bookstore.domain.review.service.AllListReviewService;
import com.study.bookstore.domain.review.service.CreateReviewService;
import com.study.bookstore.domain.review.service.DeleteReviewService;
import com.study.bookstore.domain.review.service.ListReviewService;
import com.study.bookstore.domain.review.service.ReadReviewService;
import com.study.bookstore.domain.review.service.UpdateReviewService;
import com.study.bookstore.domain.user.entity.User;
import jakarta.persistence.Column;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class ReviewFacade {


  private final DeleteReviewService deleteReviewService;
  private final UpdateReviewService updateReviewService;
  private final ListReviewService listReviewService;
  private final ReadReviewService readReviewService;
  private final ReviewRepository reviewRepository;
  private final OrderRepository orderRepository;
  private final CreateReviewService createReviewService;
  private final ReadMemberService readMemberService;

  //리뷰 추가
  public void createReview(CreateReviewReqDto req, Member member) {
    //1.ReadReviewService에서 책 조회
    Book book = readReviewService.getBookWithReviews(req.bookId());//책 조회

    if(req.rating()== null){
      throw new IllegalArgumentException("평점을 입력해 주세요.");
    }
    //2.평점 검증
    if (req.rating() < 0.0 || req.rating() > 5.0 || req.rating() % 0.5 != 0) {
      throw new IllegalArgumentException("평점은 0.0DPTJ 5.0사이의 값이어야하며,0.5단위여야 합니다. ");
    }
    //3. 사용자가 이미 해당 책에 리뷰를 작성했는지 확인
    boolean reviewedBook = reviewRepository.existsByMemberAndBook(member, book);
    if (reviewedBook) {
      throw new RuntimeException("중복된 리뷰입니다.");
    }
    //4.사용자가 해당 책을 구매했는지 확인
    boolean purchasedBook = orderRepository.existsByMemberAndOrderItemsBook(member, book);
    if (!purchasedBook) {
      throw new RuntimeException("책을 구매한 사용자만 리뷰 작성이 가능합니다.");
    }

    //5.CreateReviewReqDto에서 Review객체로 변환
    Review review = req.toReview(book, member);
    createReviewService.createReview(review);  // req와 user 전달
  }

  //리뷰 삭제
  public void deleteReview(Long reviewId, Member member) {
    deleteReviewService.deleteReview(reviewId, member);
  }

  //리뷰 수정
  public void updateReview(Long reviewId, UpdateReviewReqDto req, Member member) {

    //리뷰 조회(책 상태도 확인됨)
    Review review = readReviewService.getReviewById(reviewId);

    //리뷰 작성자와 현재 로그인한 사용자가 같은 지 확인
    if (!review.getMember().getId().equals(member.getId())) {
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
    updateReviewService.updateReview(review);
  }

  //특정 책에 대한 리뷰 확인
  public List<ReviewListRespDto> getReviewsForBook(Long bookId) {

    List<Review> reviews = readReviewService.findByBookId(bookId);
    return listReviewService.getReviewsForBook(reviews);
  }


  // 전체 리뷰 확인
  public Page<AllReviewListRespDto> getAllReviews(Pageable pageable) {
// 1. 전체 리뷰 목록 조회 (Review 객체)
    Page<Review> reviews = readReviewService.getAllReviews(pageable);
// 2. 각 리뷰를 AllReviewListRespDto로 변환
    List<AllReviewListRespDto> reviewList = reviews.stream()
        .map(review -> {
          // 리뷰가 속한 책이 삭제되었는지 확인
          if (review.getBook() != null && review.getBook().isDeleted()) {
            return null; // 삭제된 책에 속한 리뷰는 제외
          }

          // 리뷰가 삭제되지 않은 책에 해당하는 경우, AllReviewListRespDto 반환
          return new AllReviewListRespDto(
              review.getReviewId(),
              review.getContent(),
              review.getRating(),
              review.getMember() != null ? review.getMember().getName() : "Unknown",
              review.getCreatedDate(),
              review.getUpdatedDate()
          );
        })
        .filter(Objects::nonNull) // null을 제외하고 결과 반환
        .collect(Collectors.toList());

    // List를 Page로 변환해서 반환
    return new PageImpl<>(reviewList, pageable, reviews.getTotalElements());
  }
  public Member getMemberByEmail(String email){
    return readMemberService.findMemberByEmail(email);
  }
}
