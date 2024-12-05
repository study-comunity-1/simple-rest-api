package com.study.bookstore.domain.book.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType; // Jakarta CascadeType을 임포트
import org.hibernate.annotations.Cascade; // Hibernate Cascade 어노테이션 임포트
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Book extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id")
  private Long bookId; // 책 고유 인덱스

  @Column(name = "title", nullable = false)
  @Comment("책 제목")
  private String title;

  @Column(name = "author", nullable = false)
  @Comment("저자")
  private String author;

  @Column(name = "publisher", nullable = false)
  @Comment("출판사")
  private String publisher;

  @Column(name = "price", nullable = false)
  @Comment("가격")
  private int price;

  @Column(name = "stock", nullable = false)
  @Comment("재고")
  private int stock;

  @Column(name = "published_Date")
  @Comment("출판 일시")
  private LocalDate publishedDate;

  @Column(name = "page")
  @Comment("페이지 수")
  private int page;

  @Column(name = "description", nullable = true)
  @Comment("책 소개")
  private String description;

  @Column(name = "isbn", nullable = false, unique = true)
  @Comment("책 코드")
  private String isbn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id",  nullable = false) // FK 설정
  @JsonIgnore // 순환 참조 방지
  private Category category; // 카테고리와 객체 자체와 연결되며, 이를 통해 Book 클래스에서 카테고리의 이름이나 설명 같은 속성에 바로 접근할 수 있게 함.

  // 리뷰와의 관계 설정 (책 삭제 시 리뷰도 함께 삭제)
  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Review> reviews;

  //논리적 삭제 상태를 추가
  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  //책을 논리적으로 삭제하는 메서드
  public void markAsDeleted(){
    if(this.isDeleted){
      throw new IllegalStateException("이미 삭제된 책입니다.");
    }
    this.isDeleted = true;
  }
  //책 상태를 복구하는 메서드
  public void markAsRestored(){
    this.isDeleted = false;
  }

  // stock 값을 설정하는 메서드
  public void setStock(int stock) {
    this.stock = stock;
  }

  public void updateFrom(UpdateBookReqDto req) {
    this.title = req.title();
    this.author = req.author();
    this.publisher = req.publisher();
    this.price = req.price();
    this.stock = req.stock();
    this.publishedDate = req.publishedDate();
    this.page = req.page();
    this.description = req.description();
    this.isbn = req.isbn();
    // updatedDate, createdDate는 JPA에서 자동으로 관리
  }

  //재고 감소
  public void buyBook(int quantity){
    if(this.isDeleted){
      throw new IllegalStateException("삭제된 책은 주문할 수 없습니다.");
    }
    if(quantity <= 0){
      throw new IllegalStateException("주문 수량은 1 이상이어야 합니다.");
    }
    if(this.stock < quantity){
      throw new IllegalStateException("재고가 부족합니다");
    }
    this.stock -= quantity;
  }

  //책 취소 시 재고 증가 메서드(결제 취소로 상태가 변경될 때)
  public void returnBook(int quantity){
    if(this.isDeleted){
      throw new IllegalStateException("삭제된 책은 주문을 취소할 수 없습니다.");
    }
    if(quantity <= 0){
      throw new IllegalStateException("취소 수량은 1 이상이어야 합니다.");
    }

    this.stock += quantity;
    System.out.println("변경된 재고 " + this.stock);//변경된 재고 출력
  }
  //재고 확인을 위한 메서드
  public int getStock(){
    return this.stock;
  }
}
