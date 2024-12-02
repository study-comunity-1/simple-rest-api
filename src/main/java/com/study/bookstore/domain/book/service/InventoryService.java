package com.study.bookstore.domain.book.service;


import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class InventoryService {


  private final BookRepository bookRepository;
  private final ReadBookService readBookService;

  //책 재고 확인
  public int getInventory(Long bookId) {
    Book book = readBookService.findBookById(bookId);
    return book.getStock();
  }

  //재고 추가
  public int addInventory(Book book, int addBookAmount) {
    if (addBookAmount <= 0) {
      throw new RuntimeException("추가할 수량은 0보다 커야 합니다.");
    }
    book.setStock(book.getStock() + addBookAmount);
    bookRepository.save(book);
    return book.getStock();
  }

  //재고 삭제
  public int removeInventory(Book book, int removeBookAmount) {
    if (removeBookAmount <= 0) {
      throw new RuntimeException("제거할 수량은 0보다 커야 합니다.");
    }
    if (book.getStock() < removeBookAmount) {
      throw new RuntimeException("책의 재고가 부족합니다.");
    }
    book.setStock(book.getStock() - removeBookAmount);
    bookRepository.save(book);
    return book.getStock();
  }
}
