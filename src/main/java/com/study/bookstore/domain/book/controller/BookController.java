package com.study.bookstore.domain.book.controller;

import com.study.bookstore.domain.book.dto.req.CreateBookReqDto;
import com.study.bookstore.domain.book.dto.req.UpdateBookReqDto;
import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.book.entity.Book;
import com.study.bookstore.domain.book.entity.repository.BookRepository;
import com.study.bookstore.domain.book.facade.BookFacade;
import com.study.bookstore.domain.book.service.CreateBookService;
import com.study.bookstore.domain.book.service.GetBookListService;
import com.study.bookstore.domain.book.service.InventoryService;
import com.study.bookstore.domain.book.service.SearchBookService;
import com.study.bookstore.domain.book.service.UpdateBookService;
import com.study.bookstore.domain.book.service.DeleteBookService;
import com.study.bookstore.domain.book.service.GetBookDetailService;
import com.study.bookstore.domain.book.service.UpdateStockService;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.enums.Role;
import com.study.bookstore.domain.user.entity.UserType;
import com.study.bookstore.global.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.study.bookstore.domain.user.entity.User;


@Tag(name = "Book", description = "책 API")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

  private final BookFacade bookFacade;
  private final JwtUtil jwtUtil;

  @Operation(summary = "책 추가", description = "책 추가 시 관리자만 가능")
  @PostMapping("/categories/{categoryId}")
/*
  public ResponseEntity<String> addBook(@PathVariable Long categoryId,
      @RequestBody CreateBookReqDto req, HttpSession session)
 {

   유저 객체에 세션정보를 받아온다.
    User user = (User) session.getAttribute("user");
    //유저가 널 값이거나 세션에 로그인 정보가 없는 경우
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
    }
    //1. 유저의 타입을 확인
    UserType userType = user.getUserType();
    //유저가 고객인 경우에는 권한이 없습니다.
    if (userType == null || userType == UserType.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    //관리자인 경우에는 책 추가 가능
    else {
      try {
        bookFacade.createBook(categoryId, req);
        return ResponseEntity.ok().body("책 추가가 완료되었습니다.");
      } catch (Exception e) {
        // 예외 처리
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
    }
  }
  */
  public ResponseEntity<String> addBook(@PathVariable Long categoryId,
      @RequestBody CreateBookReqDto req,
      HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization");
      if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 실패: Authorizaion 헤더가 없거나 형식이 올바르지 않습니다.");
      }
      String jwtToken = token.substring(7);

      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      Member member = bookFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      Role role = member.getRole();
      if (role == null || role == role.USER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      }

      bookFacade.createBook(categoryId, req);
      return ResponseEntity.ok().body("책 추가 완료");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
          .body(e.getMessage());
    }
  }

  @Operation(summary = "책 목록", description = "책 목록을 확인합니다.")
  @GetMapping
  public ResponseEntity<List<GetBookRespDto>> getBookList(
      @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
    List<GetBookRespDto> bookList = bookFacade.getBookList(pageNumber, pageSize);
    return ResponseEntity.ok(bookList);
  }

  @Operation(summary = "책 상세 조회", description = "책의 상세 정보를 확인합니다.")
  @GetMapping("{bookId}")
  public ResponseEntity<?> getBookDetail(@PathVariable Long bookId) {
    try {
      GetBookRespDto bookDetail = bookFacade.getBookListDetail(bookId);
      return ResponseEntity.ok(bookDetail);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @Operation(summary = "책 삭제", description = "책 삭제 관리자만 가능")
  @DeleteMapping("{bookId}")
  public ResponseEntity<String> deleteBook(@PathVariable Long bookId, HttpServletRequest request) {

    try {
      // 1. Authorization 헤더에서 JWT 토큰 추출
      String token = request.getHeader("Authorization");
      if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 실패: Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
      }

      String jwtToken = token.substring(7); // "Bearer " 이후의 토큰만 추출

      // 2. 토큰에서 이메일 추출
      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      // 3. 이메일로 사용자 정보 조회
      Member member = bookFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      // 4. 권한 확인
      Role role = member.getRole();
      if (role == null || role == role.USER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      }

      bookFacade.deleteBook(bookId); //책 삭제 처리
      return ResponseEntity.ok().body("책 삭제가 완료되었습니다.");
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("책 삭제 실패: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("책 삭제 중 오류가 발생했습니다." + e.getMessage());
    }
  }

  @Operation(summary = "책 수정", description = "책 수정 관리자만 가능")
  @PutMapping("{bookId}")
  public ResponseEntity<String> updateBook(@PathVariable Long bookId, HttpServletRequest request,
      @RequestBody UpdateBookReqDto req) {
    try {
      // 1. Authorization 헤더에서 JWT 토큰 추출
      String token = request.getHeader("Authorization");
      if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 실패: Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
      }

      String jwtToken = token.substring(7); // "Bearer " 이후의 토큰만 추출

      // 2. 토큰에서 이메일 추출
      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      // 3. 이메일로 사용자 정보 조회
      Member member = bookFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      // 4. 권한 확인
      Role role = member.getRole();
      if (role == null || role == role.USER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      }

      bookFacade.updateBook(req, bookId);
      return ResponseEntity.ok().body("책 수정이 완료 되었습니다.");
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @Operation(summary = "책 재고 확인", description = "단순 재고 확인")
  @GetMapping("/inventory/{bookId}")
  public ResponseEntity<?> getBookInventory(@PathVariable Long bookId, HttpServletRequest request) {

    try {
      // 1. Authorization 헤더에서 JWT 토큰 추출
      String token = request.getHeader("Authorization");
      if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 실패: Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
      }

      String jwtToken = token.substring(7); // "Bearer " 이후의 토큰만 추출

      // 2. 토큰에서 이메일 추출
      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      // 3. 이메일로 사용자 정보 조회
      Member member = bookFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      // 4. 권한 확인
      Role role = member.getRole();
      if (role == null || role == role.USER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      }
      int stock = bookFacade.getBookInventory(bookId);
      return ResponseEntity.ok(stock);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(e.getMessage());
    }
  }

  @Operation(summary = "책 재고 추가")
  @PostMapping("/inventory/add/{bookId}")
  public ResponseEntity<String> addBookInventory(@PathVariable Long bookId, HttpServletRequest request,
      @RequestParam int addBookAmount) {

    try {
      // 1. Authorization 헤더에서 JWT 토큰 추출
      String token = request.getHeader("Authorization");
      if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("인증 실패: Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
      }

      String jwtToken = token.substring(7); // "Bearer " 이후의 토큰만 추출

      // 2. 토큰에서 이메일 추출
      String email = jwtUtil.getUserId(jwtToken);
      if (email == null) {
        return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
      }

      // 3. 이메일로 사용자 정보 조회
      Member member = bookFacade.getMemberByEmail(email);
      if (member == null) {
        return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
      }

      // 4. 권한 확인
      Role role = member.getRole();
      if (role == null || role == role.USER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      }
      int stock = bookFacade.addBookInventory(bookId, addBookAmount);
      return ResponseEntity.ok("재고가 추가되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(e.getMessage());
    }
  }

  @Operation(summary = "책 재고 삭제")
  @DeleteMapping("/inventory/remove/{bookId}")
  public ResponseEntity<String> removeBookInventory(@PathVariable Long bookId, HttpServletRequest request,
      @RequestParam int removeBookAmount) {
    // 1. Authorization 헤더에서 JWT 토큰 추출
    String token = request.getHeader("Authorization");
    if (token == null || !token.startsWith("Bearer ")) {
      return ResponseEntity.badRequest().body("인증 실패: Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
    }

    String jwtToken = token.substring(7); // "Bearer " 이후의 토큰만 추출

    // 2. 토큰에서 이메일 추출
    String email = jwtUtil.getUserId(jwtToken);
    if (email == null) {
      return ResponseEntity.badRequest().body("로그인된 사용자가 아닙니다.");
    }

    // 3. 이메일로 사용자 정보 조회
    Member member = bookFacade.getMemberByEmail(email);
    if (member == null) {
      return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
    }

    // 4. 권한 확인
    Role role = member.getRole();
    if (role == null || role == role.USER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
    }
    try {
      int stock = bookFacade.removeBookInventory(bookId, removeBookAmount);
      return ResponseEntity.ok("재고가 삭제 되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(e.getMessage());
    }
  }

  @Operation(summary = "책 카테고리 검색 및 정렬 기능")
  @GetMapping("/search")
  public ResponseEntity<Map<String, Object>> getBooks(
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "title") String sort
  ) {
    // Pageable 객체 생성 (페이지, 크기, 정렬)
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
    // Service로 pageable 전달
    Page<Book> books = bookFacade.searchBooks(categoryId, search, pageable);
    // 필요한 데이터만 맵으로 반환
    Map<String, Object> response = new HashMap<>();
    response.put("books", books.getContent());  // 책 목록
    response.put("currentPage", books.getNumber());  // 현재 페이지
    response.put("totalItems", books.getTotalElements());  // 전체 아이템 수
    response.put("totalPages", books.getTotalPages());  // 전체 페이지 수

    return ResponseEntity.ok(response);
  }
}