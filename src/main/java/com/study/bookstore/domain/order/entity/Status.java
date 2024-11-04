package com.study.bookstore.domain.order.entity;

public enum Status {
  PENDING,
  // 결제 대기 (유저가 주문을 한 직후, 결제 전 재고 확인 등을 위한 임시 상태)
  READY_FOR_PAYMENT,
  // 결제 요청 (확인 후 결제를 진행할 준비가 된 상태)
  PAYMENT_COMPLETED,
  // 결제 완료 (결제가 정상적으로 처리된 상태)
  PAYMENT_FAILED,
  // 결제 실패 (재고나 금액 등의 문제로 결제가 실패한 상태)
  CANCELLED,
  // 결제 취소 (사용자의 요청으로 결제가 취소된 상태)
  PREPARING_FOR_SHIPPING,
  // 배송 준비중 (결제 완료 후 배송 준비중인 상태)
  SHIPPING,
  // 배송중 (결제 완료 후 다음날 배송중 상태)
  DELIVERED
  // 배송 완료 (2-3일차 배송 완료 상태)
}
