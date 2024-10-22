package com.study.bookstore.domain.book.service;

import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.global.mapper.book.BookMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GetBookListService {
  private final BookMapper bookMapper; // BookMapper 의존성 주입

  public List<GetBookRespDto> getBookList(int pageNumber, int pageSize) {
    int offset = (pageNumber - 1) * pageSize;

    List<Book> books = bookMapper.getBookListPagination(pageSize, offset);

    // of 메서드를 사용해 Book 엔티티를 GetBookRespDto로 변환
    return books.stream()
        .map(GetBookRespDto::of)
        .collect(Collectors.toList());
  }
}
