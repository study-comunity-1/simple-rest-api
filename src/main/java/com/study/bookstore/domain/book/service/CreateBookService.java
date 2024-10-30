package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.mapper.book.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateBookService {
  private final BookRepository bookRepository;

  public Book addBook(CreateBookReqDto req){
    Book book = req.of(); //DTO를 엔티티로 변환
    return bookRepository.save(book);

  }
}