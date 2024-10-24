package com.study.bookstore.domain.book.service;


import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.global.mapper.book.BookMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateBookService {

  private final BookMapper bookMapper;

  public void updateBook(UpdateBookReqDto req, Long id){
    Book bookToUpdate = req.of();
    bookToUpdate.setId(id);//id 세팅
    bookToUpdate.setUpdatedDate(LocalDateTime.now()); // 업데이트 시간 설정
    bookMapper.updateBook(bookToUpdate);
  }
}
