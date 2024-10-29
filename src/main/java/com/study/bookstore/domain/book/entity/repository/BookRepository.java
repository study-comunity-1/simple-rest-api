package com.study.bookstore.domain.book.entity.repository;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {



}