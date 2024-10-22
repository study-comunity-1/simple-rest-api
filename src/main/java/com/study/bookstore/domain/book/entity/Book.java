package com.study.bookstore.domain.book.entity;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Book {
  private Long id; // 책 고유 인덱스
  private String title; // 책 제목
  private String author; // 저자
  private String publisher; // 출판사
  private int price; // 가격
  private int stock; // 재고
  private LocalDateTime publishedDate; // 출판 일시
  private int page; // 페이지 수
  private String category; // 카테고리
  private String description; // 책 소개
  private String isbn; // 책 코드
  private LocalDateTime createdDate; // 책 레코드 생성일시(책 정보가 처음 데이터베이스에 저장될 때 설정되고, 이후에는 변경되지 않음)
  private LocalDateTime updatedDate; // 수정일시(책 필드가 수정될 때마다 업데이트 됨)
}
