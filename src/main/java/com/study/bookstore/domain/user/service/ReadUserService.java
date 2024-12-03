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
    return userRepository.findByEmailAndIsDeleteFalse(email).orElse(null);
  }

  public User readUserByUserId(Long userId) {
    return userRepository.findByIdAndIsDeleteFalse(userId)
        .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
  }

  public User readUserByNick(String nick) {
    return userRepository.findByNickAndIsDeleteFalse(nick).orElse(null);
  }

  public User readUserByPhone(String phone) {
    return userRepository.findByPhoneAndIsDeleteFalse(phone).orElse(null);
  }
}
