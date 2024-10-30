package com.study.bookstore.domain.cart.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AddToCartService {

  private final BookRepository bookRepository;

  public void addToCart(Long bookId, HttpSession session) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow((() -> new NoSuchElementException("해당 ID의 책은 존재하지 않습니다.")));

    Set<Long> cart = (Set<Long>) session.getAttribute("cart");
    // 장바구니에 같은 책이 여러번 들어가는것을 방지하기위해 Set사용

    if(cart == null) {
      cart = new HashSet<>();
      session.setAttribute("cart", cart);
      // 장바구니가 비어있는경우 생성
    }

    cart.add(bookId);
    // 장바구니에 해당 책을 넣어준다
  }
}
