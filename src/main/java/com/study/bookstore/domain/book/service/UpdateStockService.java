package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateStockService {

  private final BookRepository bookRepository;

  // 책 주문시 재고 -- (결제 대기 -> 결제요청으로 상태가 변경될 때)
  public void buyBook(Long bookId, int quantity) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("해당 책은 존재하지 않습니다."));

    //1. 삭제된 책인지 확인
    if(book.isDeleted()){
      throw new RuntimeException("삭제된 책은 주문할 수 없습니다");
    }
    //재고 감소 메서드 호출
    book.buyBook(quantity);
    //상태 저장
    bookRepository.save(book);
  }

  // 주문 취소시 재고 ++ (결제취소로 상태가 변경될 때)
  public void returnBook(Long bookId, int quantity) {
    //삭제된 책인지 확인
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("해당 책은 존재하지 않습니다."));

    //재고 증가 메서드 호출
    book.returnBook(quantity);
    //상태 저장
    bookRepository.save(book);

  }

}
