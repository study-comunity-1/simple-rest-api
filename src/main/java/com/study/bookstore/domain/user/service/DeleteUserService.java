package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DeleteUserService {

  private final UserRepository userRepository;

  public void deleteUser(HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      throw new RuntimeException("회원탈퇴는 로그인 후 가능합니다.");
    } else {
      userRepository.deleteById(user.getUserId());
    }
  }
}
