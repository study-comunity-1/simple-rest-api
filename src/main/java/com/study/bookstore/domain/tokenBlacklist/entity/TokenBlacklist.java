package com.study.bookstore.domain.tokenBlacklist.entity;

import com.study.bookstore.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token_blacklist")
public class TokenBlacklist extends BaseTimeEntity {

  @Id
  private String tokenId;

  @Column(nullable = false)
  private LocalDateTime expTime;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expTime);
    // 현재 시간이 만료시간보다 이후 => 토큰이 만료됨 => true를 반환
  }
}
