package com.study.bookstore.domain.category.service;

import com.study.bookstore.domain.category.dto.req.CreateCategoryReqDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateCategoryService {

  private final CategoryRepository categoryRepository;

  public Category addCategory(CreateCategoryReqDto req) {

    Category category = req.of();
    return categoryRepository.save(category);

  }


}
