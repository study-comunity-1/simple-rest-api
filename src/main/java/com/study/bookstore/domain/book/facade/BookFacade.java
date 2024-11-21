package com.study.bookstore.domain.book.facade;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import com.study.bookstore.domain.book.service.InventoryService;
import com.study.bookstore.domain.book.service.SearchBookService;
import com.study.bookstore.domain.book.service.UpdateBookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookFacade {

  private final CreateBookService createBookService;
  private final GetBookListService getBookListService;
  private final com.study.bookstore.domain.book.service.DeleteBookService DeleteBookService;
  private final UpdateBookService updateBookService;
  private final com.study.bookstore.domain.book.service.GetBookDetailService GetBookDetailService;
  private final InventoryService inventoryService;
  private final SearchBookService searchBookService;

  //책 추가
  public void createBook(Long categoryId, CreateBookReqDto req){
    createBookService.addBook(categoryId, req);
  }
  //책 목록
  public List<GetBookRespDto> getBookList(int pageNumber, int pageSize){
    return getBookListService.getBookList(pageNumber, pageSize);
  }
  //책 상세 조회
  public GetBookRespDto getBookListDetail(Long bookId){
    return GetBookDetailService.getBookDetail(bookId);
  }
  //책 삭제
  public void deleteBook(Long bookId){
    DeleteBookService.deleteBook(bookId);
  }
  //책 수정
  public void updateBook(UpdateBookReqDto req, Long bookId){
    updateBookService.updateBook(req, bookId);
  }
  //책 재고 확인
  public int getBookInventory(Long bookId){
    return inventoryService.getInventory(bookId);
  }
  //책 재고 추가
  public int addBookInventory(Long bookId, int addBookAmount){
    return inventoryService.addInventory(bookId, addBookAmount);
  }
  //책 재고 삭제
  public int removeBookInventory(Long bookId, int removeBookAmount){
    return inventoryService.removeInventory(bookId, removeBookAmount);
  }
  //책 카테고리 검색 및 정렬 기능
  public Page<Book> searchBooks(Long categoryId, String search, Pageable pageable){
    return searchBookService.getBooks(categoryId, search, pageable);
  }

}
