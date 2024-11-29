package com.study.bookstore.domain.cart.facade;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.service.ReadBookService;
import com.study.bookstore.domain.cart.dto.resp.GetCartListRespDto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class CartFacade {

  private final ReadBookService readBookService;

  public void addToCart(Long bookId, HttpSession session) {
    try {
      Book book = readBookService.findBookById(bookId);

      Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

      if (cart == null) {
        cart = new HashMap<>();
        session.setAttribute("cart", cart);
      }

      cart.put(bookId, cart.getOrDefault(bookId, 0) + 1);
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("존재하지 않는 책입니다.");
    }
  }

  public List<GetCartListRespDto> getCartList(HttpSession session) {
    Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

    List<GetCartListRespDto> getCartList = new ArrayList<>();
    int totalQuantity = 0;

    if (cart == null) {
      return getCartList;
    }

    for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
      Long bookId = entry.getKey();
      int quantity = entry.getValue();
      Book book = readBookService.findBookById(bookId);

      if (book != null) {
        totalQuantity += quantity;
        getCartList.add(new GetCartListRespDto(book.getTitle(), quantity, 0));
      }
    }

    for (int i = 0; i < getCartList.size(); i++) {
      getCartList.set(i, new GetCartListRespDto(
          getCartList.get(i).title(),
          getCartList.get(i).quantity(),
          totalQuantity));
    }

    return getCartList;
  }
}
