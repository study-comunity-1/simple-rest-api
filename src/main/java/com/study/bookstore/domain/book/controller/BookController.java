package com.study.bookstore.domain.book.controller;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import com.study.bookstore.domain.book.service.deleteBookService;
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
  private final deleteBookService deleteBookService;


  @Operation(summary = "책 추가", description = "책 추가 시 관리자만 가능")
  @PostMapping
  public ResponseEntity<?> addBook(@RequestBody CreateBookReqDto req, HttpSession session) {

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
      createBookService.addBook(req);
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


  @Operation(summary = "책 삭제", description = "책을 삭제합니다.")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteBook(@PathVariable Long id, HttpSession session) {

    User user = (User) session.getAttribute("user");

    //유저가 널 값이면
    if (user == null) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요");
    }

    // 유저 타입 확인
    UserType userType = user.getUserType();

      // 유저타입이 널값이거나 글쓴 유저와 로그인한 유저가 다르다면
      if (userType == null || userType.equals(UserType.USER)) { // UserType은 enum으로 가정
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
      } else {
        deleteBookService.deletebook(id);
        return ResponseEntity.ok().body("책 삭제가 완료되었습니다.");
      }
    }
  }



