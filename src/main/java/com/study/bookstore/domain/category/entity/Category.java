package com.study.bookstore.domain.category.entity;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long categoryId;

  @Column(nullable = false, unique = true)
  private String categoryName;

  // 카테고리와 책의 관계 설정 (Category : Book 1:N)
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Book> books = new ArrayList<>(); // 해당 카테고리에 속하는 책 목록

  public void setCategoryName(String categoryName){
    this.categoryName = categoryName;
  }
}
