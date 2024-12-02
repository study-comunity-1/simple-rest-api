package com.study.bookstore.domain.review.service;

import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.domain.review.entity.repository.ReviewRepository;
import com.study.bookstore.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadReviewService {

  private final ReviewRepository reviewRepository;

  public Page<Review> readReviewPage(User user, Pageable pageable) {
    return reviewRepository.findByUser(user, pageable);
  }
}
