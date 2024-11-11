package com.study.bookstore.domain.book.entity.repository;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
  Page<Book> findAll(Pageable pageable);
  @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.title LIKE %:title% AND b.author LIKE %:author%")
  Page<Book> findBooksByCategoryAndTitleAndAuthor(
      @Param("categoryId") Long categoryId,
      @Param("title") String title,
      @Param("author") String author,
      Pageable pageable);

}
