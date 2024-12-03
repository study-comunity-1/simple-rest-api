package com.study.bookstore.domain.user.entity.repository;

import com.study.bookstore.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  // isDelete가 false인 email로 사용자 조회
  @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDelete = false")
  Optional<User> findByEmailAndIsDeleteFalse(String email);

  // isDelete가 false인 userId로 사용자 조회
  @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.isDelete = false")
  Optional<User> findByIdAndIsDeleteFalse(Long userId);
  // 반환되는 값이 없을 수도 있는 경우에 대비하기위해 Optional사용 (null이 반환되는 경우를 대비하여)

  // isDelete가 false인 nick으로 사용자 조회
  @Query("SELECT u FROM User u WHERE u.nick = :nick AND u.isDelete = false")
  Optional<User> findByNickAndIsDeleteFalse(String nick);

  // isDelete가 false인 phone으로 사용자 조회
  @Query("SELECT u FROM User u WHERE u.phone = :phone AND u.isDelete = false")
  Optional<User> findByPhoneAndIsDeleteFalse(String phone);
}
