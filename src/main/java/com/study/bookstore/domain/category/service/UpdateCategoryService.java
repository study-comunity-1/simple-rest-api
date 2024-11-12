package com.study.bookstore.domain.category.service;


import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateCategoryService {

  private final CategoryRepository categoryRepository;

  public void updateCategory(String categoryName, Long categoryId) {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
    category.setCategoryName(categoryName);
    categoryRepository.save(category);
  }


}
