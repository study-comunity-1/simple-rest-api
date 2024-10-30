package com.study.bookstore.domain.book.entity;


import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")

public class Book extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="book_id")
  private Long bookId; // 책 고유 인덱스

  @Column(name="title", nullable = false)
  @Comment("책 제목")
  private String title;

  @Column(name="author", nullable = false)
  @Comment("저자")
  private String author;

  @Column(name="publisher", nullable = false)
  @Comment("출판사")
  private String publisher;

  @Column(name="price", nullable = false)
  @Comment("가격")
  private int price;

  @Column(name="stock", nullable = false)
  @Comment("재고")
  private int stock;

  @Column(name="published_Date")
  @Comment("출판 일시")
  private LocalDate publishedDate;

  @Column(name="page")
  @Comment("페이지 수")
  private int page;

  @Column(name="category_id", nullable = false)
  @Comment("카테고리")
  private String categoryId;

  @Column(name="description", nullable = true)
  @Comment("책 소개")
  private String description;

  @Column(name="isbn", nullable = false)
  @Comment("책 코드")
  private String isbn;

  public void setStock(int stock) {
    this.stock = stock; // stock 값을 설정하는 메서드
  }

 /* @Column(name="created_Date")
  @Comment("책 레코드 생성일시(책 정보가 처음 데이터베이스에 저장될 때 설정되고, 이후에는 변경되지 않음)")
  private LocalDateTime createdDate;

  @Column(name="updated_Date")
  @Comment("수정일시(책 필드가 수정될 때마다 업데이트 됨)")
  private LocalDateTime updatedDate;*/


 public void updateFrom(UpdateBookReqDto req) {
   this.title = req.title();
   this.author = req.author();
   this.publisher = req.publisher();
   this.price = req.price();
   this.stock = req.stock();
   this.publishedDate = req.publishedDate();
   this.page = req.page();
   this.categoryId = req.categoryId();
   this.description = req.description();
   this.isbn = req.isbn();
   // updatedDate, createdDate는 JPA에서 자동으로 관리
 }


}
