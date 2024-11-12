package com.study.bookstore.domain.book.service;


import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.global.mapper.book.BookMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class SearchBookService {

  private final BookRepository bookRepository; // JPA Repository 사용

  public Page<Book> getBooks(Long categoryId, String search, Pageable pageable) {
    // categoryId와 search가 null일 경우 처리
    if (categoryId == null && (search == null || search.isEmpty())) {
      return bookRepository.findAll(pageable); // 둘 다 널일 경우 모든 책을 반환
    }
    return bookRepository.findBooks(categoryId, search, pageable);//널이 아니라면 검색 결과 반환
  }
}