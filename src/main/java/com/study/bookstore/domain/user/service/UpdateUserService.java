package com.study.bookstore.domain.user.service;

import com.study.bookstore.domain.user.dto.req.UpdateUserReqDto;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.entity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateUserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public void updateUser(UpdateUserReqDto req, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
      throw new RuntimeException("회원정보수정은 로그인 후 가능합니다.");
    } else {
      User userUpdate = userRepository.findById(user.getUserId())
          .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

      userUpdate.updateUser(passwordEncoder.encode(req.password()), req.nick(), req.address());

      userRepository.save(userUpdate);
    }
  }
}
