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

  public Book addBook(Long categoryId, CreateBookReqDto req) {
    // 1. isbn 중복 확인
    boolean exists = bookRepository.existsByIsbn(req.isbn());
    if (exists) {
      throw new IllegalArgumentException("ISBN이 중복되었습니다.");
    }
    // 1. 카테고리 id로 카테고리를 조회한다.
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

    // 2. CreateBookReqDto에서 Category를 포함한 Book 엔티티 생성
    Book book = req.of(category);

    // 3. 책을 데이터베이스에 저장
    return bookRepository.save(book);
  }
}