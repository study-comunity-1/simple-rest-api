package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService {

  private final UserRepository userRepository;

  public void createUser(User user) {
    userRepository.save(user);
  }
}
