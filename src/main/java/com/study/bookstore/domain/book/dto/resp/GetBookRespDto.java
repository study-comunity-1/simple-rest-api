package com.study.bookstore.domain.book.dto.resp;

import com.study.bookstore.domain.book.entity.Book;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public record GetBookRespDto(
    String title,       // 책 제목
    String author,      // 저자
    String publisher,   // 출판사
    int price,          // 가격
    int stock,          // 재고
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
        book.getDescription()
    );
  }

  // Book 리스트를 Page<GetBookRespDto>로 변환하는 정적 메서드
  public static Page<GetBookRespDto> toDtoPage(List<Book> books, Pageable pageable) {
    List<GetBookRespDto> dtoList = books.stream()
        .map(GetBookRespDto::of)
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, books.size());
  }
}