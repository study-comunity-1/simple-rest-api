package com.study.bookstore.domain.book.controller;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import com.study.bookstore.domain.book.service.InventoryService;
import com.study.bookstore.domain.book.service.SearchBookService;
import com.study.bookstore.domain.book.service.UpdateBookService;
import com.study.bookstore.domain.book.service.DeleteBookService;
import com.study.bookstore.domain.book.service.GetBookDetailService;
import com.study.bookstore.domain.user.entity.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.study.bookstore.domain.user.entity.User;


@Tag(name = "Book", description = "책 API")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

  private final CreateBookService createBookService;
  private final GetBookListService getBookListService;
  private final DeleteBookService DeleteBookService;
  private final UpdateBookService updateBookService;
  private final GetBookDetailService GetBookDetailService;
  private final InventoryService inventoryService;
  private final SearchBookService searchBookService;


  @Operation(summary = "책 추가", description = "책 추가 시 관리자만 가능")
  @PostMapping("/categories/{categoryId}/books")
  public ResponseEntity<String> addBook(@PathVariable Long categoryId, @RequestBody CreateBookReqDto req, HttpSession session) {

    //유저 객체에 세션정보를 받아온다.
    User user = (User) session.getAttribute("user");

    //유저가 널 값이거나 세션에 로그인 정보가 없는 경우
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
    }
    //1. 유저의 타입을 확인
    UserType userType = user.getUserType();

    //유저가 고객인 경우에는 권한이 없습니다.
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    //관리자인 경우에는 책 추가 가능
    else {
      createBookService.addBook(req, categoryId);
      return ResponseEntity.ok().body("책 추가가 완료되었습니다.");
    }
  }

  @Operation(summary = "책 목록", description = "책 목록을 확인합니다.")
  @GetMapping("/books")
  public ResponseEntity<List<GetBookRespDto>> getBookList(
      @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
    List<GetBookRespDto> bookList = getBookListService.getBookList(pageNumber, pageSize);
    return ResponseEntity.ok(bookList);
  }

  @Operation(summary = "책 상세 조회", description = "책의 상세 정보를 확인합니다.")
  @GetMapping("/books/{bookId}")
  public ResponseEntity<GetBookRespDto> getBookDetail(@PathVariable Long bookId) {
    GetBookRespDto bookDetail = GetBookDetailService.getBookDetail(bookId);
    return ResponseEntity.ok(bookDetail);
  }

  @Operation(summary = "책 삭제", description = "책 삭제 관리자만 가능")
  @DeleteMapping("/delete/{bookId}")
  public ResponseEntity<String> deleteBook(@PathVariable Long bookId, HttpSession session) {

    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }

    UserType userType = user.getUserType();

    if (userType == null || userType.equals(UserType.USER)) { // UserType은 enum으로 가정
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
    } else {
      DeleteBookService.deleteBook(bookId);
      return ResponseEntity.ok().body("책 삭제가 완료되었습니다.");
    }
  }

  @Operation(summary = "책 수정", description = "책 수정 관리자만 가능")
  @PutMapping("/update/{bookId}")
  public ResponseEntity<String> updateBook(@PathVariable Long bookId, HttpSession session,
      @RequestBody UpdateBookReqDto req) {
    User user = (User) session.getAttribute("user");
    //로그인 여부 확인
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");

    }
    //유저 권한 확인
    UserType userType = user.getUserType();

    if (userType == null || userType.equals(UserType.USER)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
    } else {
      updateBookService.updateBook(req, bookId);
      return ResponseEntity.ok().body("책 수정이 완료 되었습니다.");
    }

  }

  @Operation(summary = "책 재고 확인", description = "단순 재고 확인")
  @GetMapping("/inventory/{bookId}")
  public ResponseEntity<Integer> getBookInventory(@PathVariable Long bookId, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
    }
    UserType userType = user.getUserType();

    if (userType == null || userType.equals(UserType.USER)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(0);
    }
    int stock = inventoryService.getInventory(bookId);
    return ResponseEntity.ok(stock);
  }

  @Operation(summary = "책 재고 추가")
  @PostMapping("/inventory/add/{bookId}")
  public ResponseEntity<String> addBookInventory(@PathVariable Long bookId, HttpSession session,
      @RequestParam int addBookAmount) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    UserType userType = user.getUserType();

    if (userType == null || userType.equals(UserType.USER)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("확인 권한이 없습니다.");
    }
    int stock = inventoryService.addInventory(bookId, addBookAmount);
    return ResponseEntity.ok("재고가 추가되었습니다.");
  }

  @Operation(summary = "책 재고 삭제")
  @DeleteMapping("/inventory/remove/{bookId}")
  public ResponseEntity<String> removeBookInventory(@PathVariable Long bookId, HttpSession session,
      @RequestParam int removeBookAmount) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }
    UserType userType = user.getUserType();

    if (userType == null || userType.equals(UserType.USER)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("확인 권한이 없습니다.");
    }
    int stock = inventoryService.removeInventory(bookId, removeBookAmount);
    return ResponseEntity.ok("재고가 삭제 되었습니다.");
  }

  @Operation(summary = "책 카테고리검색 및 정렬 기능")
  @GetMapping
  public ResponseEntity<List<GetBookRespDto>> searchBooks(
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String author
  ) {
    List<GetBookRespDto> books = searchBookService.searchBooksBySort(categoryId, sort, title,
        author);

    return ResponseEntity.ok(books);
  }
}




