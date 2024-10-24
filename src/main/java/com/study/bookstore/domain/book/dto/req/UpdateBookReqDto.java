package com.study.bookstore.domain.book.dto.req;

import com.study.bookstore.domain.book.entity.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateBookReqDto(
    String title, // 책 제목
    String author, // 저자
    String publisher, // 출판사
    int price, // 가격
    int stock, // 재고
    LocalDate publishedDate, // 출판 일자
    int page, // 페이지 수
    String category, // 카테고리
    String description, // 책 소개
    String isbn // 책 코드
) {

  public Book of() {
    return Book.builder()
        .title(this.title)
        .author(this.author)
        .publisher(this.publisher)
        .price(this.price)
        .stock(this.stock)
        .publishedDate(this.publishedDate)
        .page(this.page)
        .category(this.category)
        .description(this.description)
        .isbn(this.isbn)
        .build();
  }
}