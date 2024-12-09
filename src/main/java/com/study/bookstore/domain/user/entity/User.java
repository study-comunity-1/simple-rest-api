package com.study.bookstore.domain.user.entity;

import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.review.entity.Review;
import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nick;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String address;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  private UserType userType = UserType.USER;
  // userType은 입력하지않으면 자동으로 USER가 된다
  // 관리자의 경우에만 회원가입시 userType = ADMIN으로 적어주기

  @OneToMany(mappedBy = "user")
  private List<Order> orders;

  @Builder.Default
  @Column(name = "is_delete", nullable = false)
  private boolean isDelete = false;

  public void updateUser(String password, String nick, String address) {
    this.password = password;
    this.nick = nick;
    this.address = address;
  }

  public Long getId() {
    return userId;
  }

  public void softDelete() {
    this.isDelete = true;
  }
}