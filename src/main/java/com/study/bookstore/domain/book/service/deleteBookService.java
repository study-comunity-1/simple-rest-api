package com.study.bookstore.domain.book.service;

import com.study.bookstore.global.mapper.book.BookMapper;
import com.study.bookstore.global.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

@Transactional
@Service
@RequiredArgsConstructor
public class deleteBookService {

  private final BookMapper bookMapper;

  //책 아이디 받아서 책 삭제
  public void deletebook(Long id) {
    bookMapper.deleteBook(id);
  }

  //public int getStock(String isbn) {
    //return bookMapper.getBookStock(isbn); //책코드를 받아서 책을 수량을 리턴한다.

  //}
}
