package com.study.bookstore.domain.review.controller;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.review.dto.req.CreateReviewReqDto;
import com.study.bookstore.domain.review.dto.req.UpdateReviewReqDto;
import com.study.bookstore.domain.review.dto.resp.AllReviewListRespDto;
import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.facade.ReviewFacade;
import com.study.bookstore.domain.review.service.AllListReviewService;
import com.study.bookstore.domain.review.service.CreateReviewService;
import com.study.bookstore.domain.review.service.DeleteReviewService;
import com.study.bookstore.domain.review.service.ListReviewService;
import com.study.bookstore.domain.review.service.UpdateReviewService;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewFacade reviewFacade;

  @Operation(summary = "리뷰 작성")
  @PostMapping
  public ResponseEntity<String> addReview(@RequestBody CreateReviewReqDto req,
      HttpSession session) {

    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    try {
      reviewFacade.createReview(req, user);  // req와 user 전달
      return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("리뷰 작성 중 오류가 발생했습니다: " + e.getMessage());
    }
  }
  @Operation(summary = "리뷰 삭제")
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    try {
      reviewFacade.deleteReview(reviewId, user);
      return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("리뷰 삭제 중 오류가 발생했습니다. " + e.getMessage());
    }
  }
  @Operation(summary = "리뷰 수정")
  @PutMapping("/{reviewId}")
  public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
      @RequestBody UpdateReviewReqDto req, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    try {
      // DTO 객체와 로그인한 사용자 객체를 서비스 메서드에 넘김
      reviewFacade.updateReview(reviewId, req, user);
      return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("리뷰 수정 중 오류 발생 " + e.getMessage());
    }
  }
  @Operation(summary = "특정 책에 대한 리뷰 확인")
  @GetMapping("/books/{bookId}/reviews")
  public ResponseEntity<?> reviewList(@PathVariable Long bookId) {
    try{
      List<ReviewListRespDto> reviews = reviewFacade.getReviewsForBook(bookId);
    return ResponseEntity.ok(reviews);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
  @Operation(summary = "전체 리뷰 확인")
  @GetMapping("/allreviews")
  public ResponseEntity<Page<AllReviewListRespDto>> reviewAll(
      @RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "createdDate") String sortBy,
      @RequestParam(defaultValue = "DESC") String direction){

    //Pageable 객체 직접 생성
    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));

    //서비스에서 리뷰 목록 가져오기
    Page<AllReviewListRespDto>reviews = reviewFacade.getAllReviews(pageable);
    return ResponseEntity.ok(reviews);
  }

}