package com.study.bookstore.domain.order.entity.repository;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.order.entity.Order;
import com.study.bookstore.domain.order.entity.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query("SELECT o FROM Order o WHERE o.status = :status and o.isDelete = false")
  // JPQL을 사용하여 상태가 전달된 status 값과 isDelete가 false인 주문들을 조회
  List<Order> findAllByStatus(Status status);

  @Query("SELECT o FROM Order o WHERE o.user.userId = :userId and o.isDelete = false")
  // Pageable을 사용하면, Spring Data JPA가 자동으로 LIMIT과 OFFSET을 처리
  // 그렇기 때문에, @Query 어노테이션을 사용할 때 Pageable을 쿼리문에 직접 넣을 필요는 없다.
  Page<Order> findAllByUser_userId(Long userId, Pageable pageable);

  // 특정 사용자가 특정 책을 주문한 적이 있는지 확인
  boolean existsByUserAndOrderItemsBook(User user, Book book);
}
