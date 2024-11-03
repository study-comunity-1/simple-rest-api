package com.study.bookstore.domain.book.service;


import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.global.mapper.book.BookMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class SearchBookService {

  private final BookMapper bookMapper;

  public List<GetBookRespDto> searchBooksBySort(Long categoryId, String sort, String title,
      String author) {

    List<Book> books = bookMapper.findBooks(categoryId, sort, title, author);
    return books.stream()
        .map(GetBookRespDto::of)
        .collect(Collectors.toList());
  }

}
