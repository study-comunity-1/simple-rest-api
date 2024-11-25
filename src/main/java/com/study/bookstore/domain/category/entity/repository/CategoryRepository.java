package com.study.bookstore.domain.category.entity.repository;

import com.study.bookstore.domain.category.entity.Category;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  //id는 카테고리의 고유 식별자인 categoryId와 같은 역할
  Optional<Category> findById(Long id);
  //카테고리 이름 중복 체크
  boolean existsByCategoryName(String categoryName);
  //다른 카테고리 중에 이름 중복 여부 확인(수정하려는 카테고리를 제외하고 다른 카테고리 이름만 중복인지 확인)
  boolean existsByCategoryNameAndCategoryIdNot(String CategoryName, Long categoryId);
  Category findByCategoryName(String CategoryName);
  Page<Category> findAll(Pageable pageable);



}
