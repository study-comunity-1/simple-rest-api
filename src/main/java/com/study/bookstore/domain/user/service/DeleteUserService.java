package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserService {

  private final UserRepository userRepository;

  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }
}
