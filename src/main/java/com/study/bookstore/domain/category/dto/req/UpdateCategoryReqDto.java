package com.study.bookstore.domain.category.dto.req;

import com.study.bookstore.domain.category.entity.Category;

public record UpdateCategoryReqDto(
    Long categoryId,//업데이트할 카테고리 아이디
    String categoryName//카테고리의 새로운 이름
) {

  public Category of() {
    return Category.builder()
        .categoryId(this.categoryId)
        .categoryName(this.categoryName)
        .build();
  }
}
