package com.study.bookstore.domain.order.entity;

import com.study.bookstore.domain.orderItem.entity.OrderItem;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = "orders")
public class Order extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  // 한 명의 유저는 여러개의 주문을 가질 수 있다.
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "total_amount", nullable = false)
  private int totalAmount;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", nullable = false)
  private PaymentMethod paymentMethod;

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @OneToMany(mappedBy = "order")
  // 하나의 order는 여러개의 orderItem을 가질 수 있다.
  // mappedBy = "order" : orderItems에 있는 order필드에 의해 매핑됨
  // cascade = CascadeType.ALL : order가 저장,삭제될 때 orderItem도 같이 저장,삭제
  private List<OrderItem> orderItems;

  @Builder.Default
  @Column(name = "is_delete", nullable = false)
  private boolean isDelete = false;
  // 삭제 요청시 실제로 데이터가 삭제되는 것이 아니라 soft delete 방식으로 처리

  public void updateTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
  }

  public void updateStatus(Status status) {
    this.status = status;

    // 상태가 결제완료로 바뀌면 결제일시에 현재 날짜 넣어주기
    if (status == Status.PAYMENT_COMPLETED) {
      this.paymentDate = LocalDateTime.now();
    }
  }

  public void softDelete() {
    this.isDelete = true;
    // isDelete = true => 삭제된 상태를 의미 (물리적인 삭제가 아니라 논리적인 삭제)
  }
}