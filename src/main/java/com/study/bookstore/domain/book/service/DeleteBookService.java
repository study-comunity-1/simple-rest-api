package com.study.bookstore.domain.book.service;

import ch.qos.logback.core.CoreConstants;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DeleteBookService {

  private final BookRepository bookRepository;
  private final ReadBookService readBookService;

  //책 아이디를 받아서 책 삭제
  public void deleteBook(Long bookId) {

    //책 찾기 및 책 삭제 여부 확인
    Book book= readBookService.findBookById(bookId);

    //논리적 삭제 처리
    book.markAsDeleted(); //idDeleted를 true로 설정
    bookRepository.save(book);//저장
  }
}
