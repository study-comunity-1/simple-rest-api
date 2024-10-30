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

  //재고 확인
  public int getInventory(Long bookId){
    Book book =  bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다.")); // 책이 없으면 예외 발생
    return book.getStock();
  }

  //재고 추가
  public int addInventory(Long bookId, int addBookAmount){
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다.")); // 책이 없으면 예외 발생


    if(addBookAmount < 0){
      throw new RuntimeException("책 수량을 정확하게 입력해 주세요.");
    }

    int newStock = book.getStock() + addBookAmount;//기존 재고에 수량 더함
    book.setStock(newStock); //새 재고로 업데이트

    bookRepository.save(book); //변경된 재고를 저장
    return newStock;
  }

  //재고 삭제
  public int removeInventory(Long bookId, int removeBookAmount){
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));//책이 없으면 예외 발생

    //기존 재고 확인
    int stock = book.getStock();
    if(stock <removeBookAmount){ //재고 부족인 경우
      throw new RuntimeException("책의 재고가 부족합니다.");
  }else {
      stock -= removeBookAmount;
    }
    book.setStock(stock);
    bookRepository.save(book);
    return stock;
  }
}
