package com.study.bookstore.domain.category.dto.resp;

import com.study.bookstore.domain.category.entity.Category;

public record GetCategoryListRespDto(
    Long categoryId,
    String categoryName
) {

    //Category엔티티를 DTO로 변환하는 메서드
    public static GetCategoryListRespDto of(Category category){
      return new GetCategoryListRespDto(
          category.getCategoryId(),
          category.getCategoryName()
      );
    }
}
