package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.global.mapper.book.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class deleteBookService {

  private final BookRepository bookRepository;

  //책 아이디 받아서 책 삭제
  public void deletebook(Long bookId) {
    bookRepository.deleteById(bookId);
  }

}
