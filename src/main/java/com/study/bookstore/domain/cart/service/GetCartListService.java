package com.study.bookstore.domain.cart.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class GetCartListService {

  private final BookRepository bookRepository;

  public List<Book> getCartList(HttpSession session) {
    Set<Long> cart = (Set<Long>) session.getAttribute("cart");

    if (cart == null) {
      return new ArrayList<>();
    }

    return bookRepository.findAllById(cart);
  }
}
