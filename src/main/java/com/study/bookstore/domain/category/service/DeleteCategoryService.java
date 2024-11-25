package com.study.bookstore.domain.category.service;

import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor

public class DeleteCategoryService {

  private final CategoryRepository categoryRepository;
  public void deleteCategory(Long categoryId){
    //카테고리 삭제
    categoryRepository.deleteById(categoryId);
  }
}
