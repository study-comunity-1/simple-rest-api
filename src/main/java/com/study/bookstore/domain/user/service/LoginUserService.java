package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.dto.req.LoginUserReqDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginUserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public void loginUser(LoginUserReqDto req, HttpSession session) {
    User byEmail = userRepository.findByEmail(req.email());
    // req.email() : 사용자가 로그인시 입력한 email 정보를 가져옴
    // userMapper.findByEmail(req.email()) : 해당 email과 일치하는 User를 가져옴
    // 만약 일치하는 정보가 없다면 byEmail == null

    if (byEmail == null || !passwordEncoder.matches(req.rawPassword(), byEmail.getPassword())) {
      // byEmail == null : 입력한 Email과 일치하는 User 계정이 없음
      // passwordEncoder.matches(req.rawPassword(), byEmail.getPassword())
      // : 입력한 password(평문)와 데이터베이스에 저장된 password(암호화)가 일치하는지 비교
      throw new RuntimeException("로그인 실패");
      // 일치하는 계정이 없는 경우 throw : 발생한 예외를 controller로 전가
    } else {
      session.setAttribute("user", byEmail);
      // 일치하는 계정이 있는 경우 : 세션에 해당 유저의 정보를 저장하여 로그인 상태를 유지함
    }
  }
}
