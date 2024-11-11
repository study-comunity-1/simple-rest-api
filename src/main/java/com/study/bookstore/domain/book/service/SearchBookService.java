package com.study.bookstore.domain.book.service;


import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.global.mapper.book.BookMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class SearchBookService {

  private final BookRepository bookRepository; // JPA Repository 사용

  public Page<GetBookRespDto> searchBooksBySort(Long categoryId, String sort, String title,
      String author, Pageable pageable) {

    // JPA Repository에서 책 데이터를 가져옵니다.
    Page<Book> books = bookRepository.findBooksByCategoryAndTitleAndAuthor(
        categoryId, title, author, pageable);

    // Page<Book>을 Page<GetBookRespDto>로 변환
    return books.map(GetBookRespDto::of); // Book -> GetBookRespDto 변환
  }
}