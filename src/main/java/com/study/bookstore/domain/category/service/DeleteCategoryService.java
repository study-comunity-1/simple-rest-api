package com.study.bookstore.domain.category.service;

import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor

public class DeleteCategoryService {

  private final CategoryRepository categoryRepository;

  public void deleteCategory(Long categoryId){
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(()-> new IllegalArgumentException("해당 카테고리가 없습니다."));

    categoryRepository.delete(category);
  }



}
