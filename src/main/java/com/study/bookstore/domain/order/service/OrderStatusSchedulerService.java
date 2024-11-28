package com.study.bookstore.domain.order.service;

import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import com.study.bookstore.domain.order.entity.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class OrderStatusSchedulerService {
// 주문상태가 '결제완료'가 되면 그 이후 자동으로 상태를 변경하도록 만들어줌

  private final OrderRepository orderRepository;

  @Scheduled(cron = "0 */2 * * * *")
  // 크론 : 초 분 시 일 월 요일 (cron = "0 0 0 * * *") => 매일 자정에 실행
  // @Scheduled(cron = "0 */2 * * * *")
  // 잘 작동하는지 확인하기위해 2분마다 상태가 변경되도록 함
  public void orderStatusScheduler() {

    // 배송중상태인 order들을 조회
    List<Order> shippingOrders = orderRepository.findAllByStatus(Status.SHIPPING);

    for (Order order : shippingOrders) {
      if (order.getStatus() == Status.SHIPPING) {
        order.updateStatus(Status.DELIVERED);
        log.info("주문 ID: {} - 배송완료 상태로 변경", order.getOrderId());
      }
    }

    // 배송준비중상태인 order들을 조회
    List<Order> preparingForShippingOrders = orderRepository.findAllByStatus(Status.PREPARING_FOR_SHIPPING);

    for (Order order : preparingForShippingOrders) {
      if (order.getStatus() == Status.PREPARING_FOR_SHIPPING) {
        order.updateStatus(Status.SHIPPING);
        log.info("주문 ID: {} - 배송중 상태로 변경", order.getOrderId());
      }
    }

    // 결제완료상태인 order들을 조회
    List<Order> completedOrders = orderRepository.findAllByStatus(Status.PAYMENT_COMPLETED);

    for (Order order : completedOrders) {
      // 결제완료 -> 배송준비중
      if (order.getStatus() == Status.PAYMENT_COMPLETED) {
        order.updateStatus(Status.PREPARING_FOR_SHIPPING);
        log.info("주문 ID: {} - 배송준비중 상태로 변경", order.getOrderId());
      }
    }
  }
}
