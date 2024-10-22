package com.study.bookstore.domain.book.controller;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book", description = "책 API")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
  private final CreateBookService createBookService;
  private final GetBookListService getBookListService;

  //책 추가
  @Operation(summary = "책 추가", description = "책을 추가합니다.")
  @PostMapping
  public ResponseEntity<?> addBook(@RequestBody CreateBookReqDto req){
    createBookService.addBook(req);
    return ResponseEntity.ok().body("책 추가가 완료되었습니다.");
  }

  // 책 목록을 가져오는 API
  @Operation(summary = "책 목록", description = "책 목록을 확인합니다.")
  @GetMapping("/books")
  public ResponseEntity<List<GetBookRespDto>> getBookList(
      @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
    List<GetBookRespDto> bookList = getBookListService.getBookList(pageNumber, pageSize);
    return ResponseEntity.ok(bookList);
  }
}
