package com.study.bookstore.domain.book.service;


import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.global.mapper.book.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateBookService {

  private final BookRepository bookRepository;

  public void updateBook(UpdateBookReqDto req, Long bookId) {
    Book existingBook = bookRepository.findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다.:ID" + bookId));

    existingBook.updateFrom(req);
    bookRepository.save(existingBook);


  }
}
