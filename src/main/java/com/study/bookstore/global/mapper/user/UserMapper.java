package com.study.bookstore.global.mapper.user;

import com.study.bookstore.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

  // 회원가입(유저 생성) - 반환형 void
  void createUser(User user);

  // 로그인(사용자에게 입력받은 EMAIL 값을 가지고 데이터베이스에서 일치하는 계정이 있는지 확인) - 반환형 User
  User findByEmail(@Param("email") String email);

  // 회원탈퇴(유저 삭제) - 반환형 void
  void deleteUser(@Param("id") Long id);

  // 회원수정 - 반환형 void
  void updateUser(User user);
}