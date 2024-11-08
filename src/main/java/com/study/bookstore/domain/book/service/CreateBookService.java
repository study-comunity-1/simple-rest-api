package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.mapper.book.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateBookService {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;

  public Book addBook(CreateBookReqDto req, Long categoryId) {

    //1.카테고리 id로 카테고리를 조회한다.
    Category category = categoryRepository.findById(req.categoryId())
        .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

    //2.dto에서 카테고리 id를 포함한 book 엔티티 생성
    Book book = req.of(category);

    //3.책을 데이터베이스에 저장
    return bookRepository.save(book);



  }
}