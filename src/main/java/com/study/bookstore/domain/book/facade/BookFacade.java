package com.study.bookstore.domain.book.facade;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import com.study.bookstore.domain.book.service.InventoryService;
import com.study.bookstore.domain.book.service.ReadBookService;
import com.study.bookstore.domain.book.service.SearchBookService;
import com.study.bookstore.domain.book.service.UpdateBookService;
import com.study.bookstore.domain.book.service.UpdateStockService;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.service.read.ReadMemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
  private final ReadBookService readBookService;
  private final ReadMemberService readMemberService;

  //책 추가
  public void createBook(Long categoryId, CreateBookReqDto req) {
    //1.ISBN 중복검증
    readBookService.validateIsbn(req.isbn());

    //2.카테고리 조회
    Category category = readBookService.getCategory(categoryId);

    //3.엔티티 생성 및 저장
    Book book = req.of(category);
    createBookService.saveBook(book);
  }

  //책 목록 조회(삭제되지 않은 책만 조회)
  public List<GetBookRespDto> getBookList(int pageNumber, int pageSize) {
    //pageable객체 생성
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

    //책 목록을 페이징 처리하여 조회하고, 삭제되지 않은 책만 필터링
    Page<Book> bookPage = getBookListService.getBookList(pageable);
    //Book엔티티를 GetBookRespDto로 변환하여 반환
    return bookPage.stream()
        .filter(readBookService::isBookNotDeleted)//삭제되지 않은 책만 필터링
        .map(GetBookRespDto::of)
        .collect(Collectors.toList());
  }

  //책 상세 조회
  public GetBookRespDto getBookListDetail(Long bookId) {
    return GetBookDetailService.getBookDetail(bookId);
  }

  //책 삭제
  public void deleteBook(Long bookId) {
    DeleteBookService.deleteBook(bookId);
  }

  //책 수정
  public void updateBook(UpdateBookReqDto req, Long bookId) {
    //1.ISBN 중복검증
    readBookService.validateIsbn(req.isbn());
    System.out.println("isbn중복 검증");
    updateBookService.updateBook(req, bookId);
  }

  //책 재고 확인
  public int getBookInventory(Long bookId) {
    Book book = readBookService.findBookById(bookId);
    return inventoryService.getInventory(bookId);
  }

  //책 재고 추가
  public int addBookInventory(Long bookId, int addBookAmount) {
    Book book = readBookService.findBookById(bookId);
    return inventoryService.addInventory(book, addBookAmount);
  }

  //책 재고 삭제
  public int removeBookInventory(Long bookId, int removeBookAmount) {
    Book book = readBookService.findBookById(bookId);
    return inventoryService.removeInventory(book, removeBookAmount);
  }

  //책 카테고리 검색 및 정렬 기능
  public Page<Book> searchBooks(Long categoryId, String search, Pageable pageable) {
    return searchBookService.getBooks(categoryId, search, pageable);
  }
  public Member getMemberByEmail(String email){
    return readMemberService.findMemberByEmail(email);
  }
}
