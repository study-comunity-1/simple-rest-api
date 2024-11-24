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

  public void addCategory(CreateCategoryReqDto req) {

    //1. 카테고리 이름 중복 체크
    boolean categoryNameExist = categoryRepository.existsByCategoryName(req.categoryName());
    if(categoryNameExist){
      throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
    }
    //2. 카테고리 이름이 없다면 카테고리 추가 가능
    Category category = req.of();
    categoryRepository.save(category);

  }

}
