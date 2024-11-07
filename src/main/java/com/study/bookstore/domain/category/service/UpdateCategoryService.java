package com.study.bookstore.domain.category.service;


import com.study.bookstore.domain.category.dto.req.UpdateCategoryReqDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateCategoryService {

  private final CategoryRepository categoryRepository;

  public void updateCategory(UpdateCategoryReqDto req) {

    Long categoryId = req.categoryId();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

    category.setCategoryName(req.categoryName());
    categoryRepository.save(category);
  }


}
