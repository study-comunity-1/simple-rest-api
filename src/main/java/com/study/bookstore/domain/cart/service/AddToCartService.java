package com.study.bookstore.domain.cart.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AddToCartService {

  private final BookRepository bookRepository;

  public void addToCart(Long bookId, HttpSession session) {
    try {
      Optional<Book> optionalBook = bookRepository.findById(bookId);

      if (optionalBook.isEmpty()) {
        throw new NoSuchElementException("해당 ID의 책은 존재하지 않습니다.");
      }

      Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

      if (cart == null) {
        cart = new HashMap<>();
        session.setAttribute("cart", cart);
      }

      cart.put(bookId, cart.getOrDefault(bookId, 0) + 1);
      // cart.getOrDefault(bookId, 0) : cart에 해당 bookId와 그 값이 존재하는지 확인 후
      // 존재한다면 해당 값을, 존재하지 않는다면 기본값으로 설정한 0을 가져온다

    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("존재하지 않는 책입니다.");
    }
  }
}
