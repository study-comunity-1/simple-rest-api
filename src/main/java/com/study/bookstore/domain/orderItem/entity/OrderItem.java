package com.study.bookstore.domain.orderItem.entity;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.order.entity.Order;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orderItems")
public class OrderItem extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long orderItemId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  // 하나의 order는 여러개의 orderItem을 가질 수 있다.
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  // 하나의 book은 여러개의 orderItem을 가질 수 있다.

  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @Column(nullable = false)
  private int quantity;

  @Column(name = "item_price", nullable = false)
  private int itemPrice;
}
