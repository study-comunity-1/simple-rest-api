package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.dto.req.UpdateUserReqDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.mapper.user.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateUserService {

  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  public void updateUser(UpdateUserReqDto req, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      throw new RuntimeException("회원정보수정은 로그인 후 가능합니다.");
    } else {
      User userUpdate = req.of(passwordEncoder.encode(req.password()), user.getId());
      userMapper.updateUser(userUpdate);
    }
  }
}
