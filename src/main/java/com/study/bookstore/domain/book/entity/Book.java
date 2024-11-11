package com.study.bookstore.domain.book.entity;


import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
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
import lombok.Setter;
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

  @Column(name = "isbn", nullable = false)
  @Comment("책 코드")
  private String isbn;

  @ManyToOne //category_id라는 칼럼이 추가되어, Category와 연결될 수 있게 한다.
  @JoinColumn(name = "category_id", insertable = false, updatable = false) // FK 설정
  private Category category; // 카테고리와 객체 자체와 연결되며, 이를 통해 Book 클래스에서 카테고리의 이름이나 설명 같은 속성에 바로 접근할 수 있게 함.

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Review> reviews;// 하나의 책에 여러 개의 리뷰가 연결됨

  public void setStock(int stock) {
    this.stock = stock; // stock 값을 설정하는 메서드
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

  // 책 주문시 재고 -- (결제 대기 -> 결제요청으로 상태가 변경될 때)
  public void buyBook(int quantity) {
    this.stock -= quantity;
  }

  // 주문 취소시 재고 ++ (결제취소로 상태가 변경될 때)
  public void returnBook(int quantity) {
    this.stock += quantity;
  }
}
