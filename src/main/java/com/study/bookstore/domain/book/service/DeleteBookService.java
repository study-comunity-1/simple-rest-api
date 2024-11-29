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

  //책 아이디를 받아서 책 삭제
  public String deleteBook(Long bookId) {

    //책 찾기
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다"));

    //논리적 삭제 상태 확인
    if (book.isDeleted()) {
      throw new IllegalStateException("이미 삭제된 책 입니다.");
    }
    //논리적 삭제 처리
    book.markAsDeleted(); //idDeleted를 true로 설정
    bookRepository.save(book);//저장

    return "책이 삭제되었습니당";
  }

}
