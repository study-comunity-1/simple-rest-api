package com.study.bookstore.domain.book.controller;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.service.CreateBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book", description = "책 API")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
  private final CreateBookService createBookService;

  //책 추가
  @Operation(summary = "책 추가", description = "책을 추가합니다.")
  @PostMapping
  public ResponseEntity<?> addBook(@RequestBody CreateBookReqDto req){
    createBookService.addBook(req);
    return ResponseEntity.ok().body("책 추가가 완료되었습니다.");
  }

}
