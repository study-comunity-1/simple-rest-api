package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.global.mapper.book.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateBookService {
  private final BookMapper bookMapper;

  public void addBook(CreateBookReqDto req){
    bookMapper.insertBook(req.of());//(req니까)Dto에서 Book 엔티티로 변환

  }
}