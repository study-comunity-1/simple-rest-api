package com.study.bookstore.domain.book.dto.req;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.category.entity.Category;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateBookReqDto(

    String title, // 책 제목
    String author, // 저자
    String publisher, // 출판사
    int price, // 가격
    int stock, // 재고
    LocalDate publishedDate, // 출판 일시
    int page, // 페이지 수
    String description, // 책 소개
    String isbn// 책 코드
) {

  //book객체를 만드는 메서드
  public Book of(Category category) {
    return Book.builder()
        .title(this.title)
        .author(this.author)
        .publisher(this.publisher)
        .price(this.price)
        .stock(this.stock)
        .publishedDate(this.publishedDate)
        .page(this.page)
        .description(this.description)
        .isbn(this.isbn)
        .category(category)
        .build();
  }
}
