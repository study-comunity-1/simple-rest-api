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

  public List<GetBookRespDto> getBookList(int pageNumber, int pageSize) {
    // Pageable 객체 생성
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // 페이지 번호는 0부터 시작하므로 -1

    // 책 목록을 페이징 처리하여 조회
    Page<Book> bookPage = bookRepository.findAll(pageable);

    // Book 엔티티를 DTO로 변환
    return bookPage.stream()
        .map(GetBookRespDto::of) // of 메서드를 사용하여 변환
        .collect(Collectors.toList());
  }
}
