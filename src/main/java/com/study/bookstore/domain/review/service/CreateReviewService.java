package com.study.bookstore.domain.review.service;


import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.review.dto.req.CreateReviewReqDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import com.study.bookstore.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateReviewService {

  private final ReviewRepository reviewRepository;
  private final OrderItemsRepository orderItemsRepository;
  private final BookRepository bookRepository;


  public Review createReview(CreateReviewReqDto req, User user) {

    Book book1 = bookRepository.findById(req.bookId())
        .orElseThrow(()-> new RuntimeException("해당 책을 찾을 수 없습니다."));

    //평점은 0.5단위이며 5점 만점, 세 가지 조건 중 하나라도 참이면 예외 발생
    if (req.rating() < 0.0 || req.rating() > 5.0 || req.rating() % 0.5 != 0) {
      throw new IllegalArgumentException("평점은 0.0에서 5.0 사이의 값이어야 하며 0.5 단위여야 합니다.");
    }

    //1.사용자가 해당 책에 대한 리뷰를 작성했는지 확인
    boolean reviewedBook = reviewRepository.existsReview(user, book1);
    if (reviewedBook) {
      throw new RuntimeException("중복된 리뷰입니다.");
    }
    //2.사용자가 해당 책을 구매했는지에 대한 확인
    boolean purchasedBook = reviewRepository.existsReview(user, book1);
    if(!purchasedBook){
      throw new RuntimeException("책을 구매한 사용자만 리뷰작성이 가능합니다.");
    }

    //3.of 메서드로 리뷰 엔티티 생성
// 리뷰 생성 및 저장
    Review review = Review.builder()
        .book(book1)
        .user(user)
        .rating(req.rating())
        .content(req.content())
        .build();

    return reviewRepository.save(review);


  }

}
