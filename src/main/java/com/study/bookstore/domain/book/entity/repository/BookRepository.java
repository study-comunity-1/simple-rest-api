package com.study.bookstore.domain.book.entity.repository;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  // ISBN을 통해 책이 존재하는지 확인하는 메서드
  boolean existsByIsbn(String isbn);

  // 전체 책 목록을 페이징 처리하여 가져오는 메서드
  Page<Book> findAll(Pageable pageable);

 // categoryId와 search 조건으로 책을 검색하는 메서드
      @Query( "SELECT b FROM Book b WHERE " +
              "(:categoryId IS NULL OR b.category.categoryId = :categoryId) AND " +
              "(:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) "
              + "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')))")
      Page<Book> findBooks( @Param("categoryId") Long categoryId,
                            @Param("search") String search, Pageable pageable);
  //삭제된 책 필터링(isDeleted가 true인 책이 조회되지 않는 쿼리
  //List<Book> findAllByIsDeletedFalse();
}


/*(:categoryId IS NULL OR b.category.categoryId = :categoryId):
categoryId가 null이면 필터링을 하지 않음. categoryId가 주어지면 해당 카테고리의 책만 반환
(:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))):
search가 null일 경우 검색을 하지 않음. search가 주어지면 책 제목(title)이나 저자(author)에서 해당 검색어를 포함하는 책을 반환
LOWER 함수와 CONCAT을 사용하여 대소문자를 구분하지 않고(LOWER) 검색어를 포함하는 책을 찾음
LIKE는 부분 일치를 의미. 예를 들어, search가 "Java"일 경우 제목이나 저자에 "Java"가 포함된 책을 모두 찾음*/