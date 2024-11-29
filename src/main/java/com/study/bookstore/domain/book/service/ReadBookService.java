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

  //책 단순 조회
  public Book findBookById(Long bookId){
    return bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
  }

  //책 삭제 여부 확인
  public void vailidateBookNotDeleted(Book book){
    if(book.isDeleted()){
      throw new RuntimeException("삭제된 책은 처리할 수 없습니다.");
    }
  }
}
