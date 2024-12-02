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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GetBookListService {

  private final BookRepository bookRepository;

  //책 목록 조회(페이징 처리는 파샤드에서 담당)
  public Page<Book> getBookList(Pageable pageable) {
    return bookRepository.findAll(pageable);
  }
}
