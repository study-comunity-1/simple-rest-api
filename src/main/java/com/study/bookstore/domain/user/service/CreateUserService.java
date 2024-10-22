package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.dto.req.CreateUserReqDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CreateUserService {

  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  public void createUser(CreateUserReqDto req) {
    User user = req.of(passwordEncoder.encode(req.password()));
    // req.password() : 유저가 회원가입시 입력한 비밀번호를 가져옴 => 평문 비밀번호
    // passwordEncoder.encode(req.password()) : 가져온 평문 비밀번호를 암호화
    // req.of(passwordEncoder.encode(req.password())) : 암호화한 비밀번호를 of()메서드의 매개변수로 전달

    log.info("user id: {}", user.getName());
    // 로깅

    userMapper.createUser(user);
  }
}
