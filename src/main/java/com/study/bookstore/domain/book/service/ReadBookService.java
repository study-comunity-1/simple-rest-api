package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadBookService {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;

  //책 조회
  public Book findBookById(Long bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

    if (book.isDeleted()) {
      throw new RuntimeException("삭제된 책입니다.");
    }
    return book;
  }

  //isbn 중복 확인
  public void validateIsbn(String isbn) {
    if (bookRepository.existsByIsbn(isbn)) {
      throw new IllegalStateException("Isbn이 중복되었습니다.");
    }
  }

  //카테고리 조회
  public Category getCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다."));
  }

  //책이 삭제되지 않았는지 확인하는 메서드
  public boolean isBookNotDeleted(Book book) {
    return !book.isDeleted();
  }

}


