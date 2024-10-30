package com.study.bookstore.domain.book.dto.resp;

import com.study.bookstore.domain.book.entity.Book;

public record GetBookRespDto(
    String title,       // 책 제목
    String author,      // 저자
    String publisher,   // 출판사
    int price,          // 가격
    int stock,          // 재고
    String categoryId,     // 카테고리
    String description  // 책 소개

) {
  // Book 엔티티를 GetBookRespDto로 변환하는 정적 팩토리 메서드
  public static GetBookRespDto of(Book book) {
    return new GetBookRespDto(
        book.getTitle(),
        book.getAuthor(),
        book.getPublisher(),
        book.getPrice(),
        book.getStock(),
        book.getCategoryId(),
        book.getDescription()
    );
  }

}
