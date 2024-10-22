package com.study.bookstore.global.mapper.user;

import com.study.bookstore.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  // 회원가입(유저 생성) - 반환형 void
  void createUser(User user);
}