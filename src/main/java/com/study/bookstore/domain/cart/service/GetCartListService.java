package com.study.bookstore.domain.cart.service;

import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.cart.dto.resp.GetCartListRespDto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class GetCartListService {

  private final BookRepository bookRepository;

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
      Book book = bookRepository.findById(bookId).orElse(null);

      if (book != null) {
        totalQuantity += quantity;
        getCartList.add(new GetCartListRespDto(book.getTitle(), quantity, 0));
        // 총 수량은 제일 마지막에 알 수 있기때문에 임시로 0을 넣음
      }
    }

    // 반복문이 끝난 후 나온 총 수량의 값을 다시 넣어줌
    for (int i = 0; i < getCartList.size(); i++) {
      getCartList.set(i, new GetCartListRespDto(
          getCartList.get(i).title(), // title, quantity는 위의 반복문에서 넣었던 값 그대로 사용
          getCartList.get(i).quantity(),
          totalQuantity));
    }

    return getCartList;
  }
}
