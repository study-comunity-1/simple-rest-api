package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadBookService {

  private final BookRepository bookRepository;

  public Book readBook(Long bookId) {
    return bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 책입니다."));
  }
}
