package com.study.bookstore.domain.category.facade;

import com.study.bookstore.domain.category.dto.req.CreateCategoryReqDto;
import com.study.bookstore.domain.category.service.CreateCategoryService;
import com.study.bookstore.domain.category.service.DeleteCategoryService;
import com.study.bookstore.domain.category.service.UpdateCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFacade {

  private final CreateCategoryService createCategoryService;
  private final UpdateCategoryService updateCategoryService;
  private final DeleteCategoryService deleteCategoryService;

  //카테고리 추가
  public void addCategory(CreateCategoryReqDto req) {
    createCategoryService.addCategory(req);
  }
  //카테고리 수정
  public void updateCategory(String categoryName, Long categoryId) {
    updateCategoryService.updateCategory(categoryName, categoryId);
  }
  //카테고리 삭제
  public void deleteCategory(Long categoryId){
    deleteCategoryService.deleteCategory(categoryId);
  }
}


