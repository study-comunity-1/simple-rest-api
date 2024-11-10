package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.review.dto.resp.ReviewListRespDto;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import com.study.bookstore.domain.user.dto.resp.UserReviewListRespDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GetUserReviewService {

  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;

  public Page<UserReviewListRespDto> getUserReview(Long userId, Pageable pageable) {
    //1.유저를 조회한다.
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

    //2.유저가 작성한 리뷰 목록을 페이지네이션으로 조회한다.
    Page<Review> reviewPage = reviewRepository.findByUser(user, pageable);

    //3.엔티티형식인 리뷰목록을 클라이언트에게 보여주기 위해 dto로 변환
    Page<UserReviewListRespDto> reviewListRespDtos = reviewPage.map(review ->
        new UserReviewListRespDto(
            review.getReviewId(),
            review.getRating(),  // rating은 이미 Double 타입이므로 캐스팅 필요 없음
            review.getContent(),
            review.getBook().getBookId(),
            review.getBook().getAuthor(),
            review.getCreatedDate()
        )
    );

    //4.dto를 포함한 응답을 반환한다.
    return reviewListRespDtos;
  }
}


