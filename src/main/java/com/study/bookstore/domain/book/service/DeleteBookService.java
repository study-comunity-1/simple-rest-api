package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.entity.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DeleteBookService {

  private final BookRepository bookRepository;

  //책 아이디를 받아서 책 삭제
  public void deleteBook(Long bookId) {
    bookRepository.deleteById(bookId);
  }

}
