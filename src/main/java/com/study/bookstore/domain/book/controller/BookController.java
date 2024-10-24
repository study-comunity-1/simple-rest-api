package com.study.bookstore.domain.book.controller;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import com.study.bookstore.domain.user.entity.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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



  @Operation(summary = "책 추가", description = "책 추가 시 관리자만 가능")
  @PostMapping
  public ResponseEntity<?> addBook(@RequestBody CreateBookReqDto req, HttpSession session){

    //유저 객체에 세션정보를 받아온다.
    User user = (User) session.getAttribute("user");

    //유저가 널 값이거나 세션에 로그인 정보가 없는 경우
    if(user==null){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
    }

    //1. 유저의 타입을 확인
    UserType userType = user.getUserType();

    //유저가 고객인 경우에는 권한이 없습니다.
    if(userType == null ||userType==UserType.USER){
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    //관리자인 경우에는 책 추가 가능
    else{
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


}
