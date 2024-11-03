package com.study.bookstore.global.mapper.book;


import com.study.bookstore.domain.book.entity.Book;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookMapper {

 /* int insertBook(Book book);//책 추가(관리자 권한 필요)
  int updateBook(Book book);//책 수정(관리자 권한 필요)
  int deleteBook(Long id); //책 삭제(관리자 권한 필요)
  int getBookStock(String isbn); //책 재고 확인
  List<Book> getBookList();//모든 책 목록 조회
  Book getBookById(Long id);//책 ID로 상세 조회
  List<Book> getBookByAuthor(String author);//작가별 책 조회
  List<Book> getBookByCategory(String category);//카테고리별 책 조회
  List<Book> getBookByTitle(String title);//제목별 책 조회
  int getTotalBooks();// 총 책 수를 가져오는 메서드
  List<Book> getBookListPagination(int pageSize, int offset); // 페이지네이션을 적용하여 책 목록을 가져오는 메서드
*/

  //카테고리 입력 및 정렬 조건에 따른 책 목록 조회
  List<Book>findBooks(@Param("categoryId") Long categoryId, @Param("sort")String sort,@Param("title") String title,
      @Param("author") String author);
}