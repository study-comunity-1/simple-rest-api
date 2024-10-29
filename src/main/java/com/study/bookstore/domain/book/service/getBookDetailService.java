package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class getBookDetailService {
  private final BookRepository bookRepository;

  public GetBookRespDto getBookDetail(Long bookId){

    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));
    return GetBookRespDto.of(book);
  }

}
