package com.study.bookstore.domain.category.service;


import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCategoryService {

  private final CategoryRepository categoryRepository;

  public void updateCategory(Category category, String categoryName) {
    //카테고리 이름 수정
    category.setCategoryName(categoryName);
    //저장
    categoryRepository.save(category);
  }
}
