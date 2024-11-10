package com.study.bookstore.domain.review.service;


import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import com.study.bookstore.domain.orderItem.entity.repository.OrderItemRepository;
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
  private final OrderRepository orderRepository;
  private final BookRepository bookRepository;

  public Review createReview(CreateReviewReqDto req, User user) {

    //book객체 찾아오기
    Book book = bookRepository.findById(req.bookId())
        .orElseThrow(() -> new RuntimeException("리뷰를 작성하려는 책을 찾을 수 없습니다."));

    Review review = req.toReview(book, user);

    //평점은 0.5단위이며 5점 만점, 세 가지 조건 중 하나라도 참이면 예외 발생
    if (req.rating() < 0.0 || req.rating() > 5.0 || req.rating() % 0.5 != 0) {
      throw new IllegalArgumentException("평점은 0.0에서 5.0 사이의 값 이어야 하며 0.5 단위여야 합니다.");
    }

    //1.사용자(로그인한)가 해당 책에 대한 리뷰를 작성했는지 확인
    boolean reviewedBook = reviewRepository.existsByUserAndBook(user, book);
    if (reviewedBook) {
      throw new RuntimeException("중복된 리뷰입니다.");
    }
    //2.사용자가 해당 책을 구매했는지에 대한 확인
    boolean purchasedBook = orderRepository.existsByUserAndOrderItemsBook(user, book);
    if (!purchasedBook) {
      throw new RuntimeException("책을 구매한 사용자만 리뷰 작성이 가능합니다.");
    }
    //리뷰 저장
    return reviewRepository.save(review);
  }
}
