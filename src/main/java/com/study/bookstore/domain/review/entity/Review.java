package com.study.bookstore.domain.review.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.user.entity.User;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;
import org.hibernate.annotations.Cascade;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"book_id", "user_id"})
})//book_id와 user_id의 조합이 유니크하도록 설정
public class Review extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id")
  private Long reviewId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  /*@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
*/
  @Column(nullable = false)
  private double rating;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false; // 기본값 false설정

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member; // User 대신 Member 사용

  //리뷰를 논리적으로 삭제하는 메서드
  public void markAsDeleted(){
    this.isDeleted = true;
  }

  public void setContent(String content) {
    this.content=content;
  }

  public void setRating(double rating) {
    this.rating=rating;
  }
}
