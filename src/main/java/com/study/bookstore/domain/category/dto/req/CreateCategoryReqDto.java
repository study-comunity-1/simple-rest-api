package com.study.bookstore.domain.category.dto.req;

import com.study.bookstore.domain.category.entity.Category;

public record CreateCategoryReqDto(

    String categoryName
) {

  public Category of() {
    return Category.builder()
        .categoryName(this.categoryName)
        .build();
  }
}
