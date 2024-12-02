package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadUserService {

  private final UserRepository userRepository;

  public User readUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User readUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
  }
}
