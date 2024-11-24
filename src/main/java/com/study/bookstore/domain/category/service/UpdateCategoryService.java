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

    //1. 카테고리 아이디로 카테고리 존재 확인
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("해당 Id의 카테고리가 존재하지 않습니다."));

    //2. 다른 카테고리 중에 이름 중복 여부 확인
    boolean isDuplicated = categoryRepository.existsByCategoryNameAndCategoryIdNot(categoryName, categoryId);
    if(isDuplicated){
      throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
    }
    category.setCategoryName(categoryName);
    categoryRepository.save(category);
  }
}
