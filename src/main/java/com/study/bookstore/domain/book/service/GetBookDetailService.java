package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GetBookDetailService {

  private final ReadBookService readBookService;

  public GetBookRespDto getBookDetail(Long bookId) {
    Book book = readBookService.findBookById(bookId);
    return GetBookRespDto.of(book);
  }
}
