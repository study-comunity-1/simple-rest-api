package com.study.bookstore.domain.category.dto.resp;

import java.util.List;

public record GetAllCategoryListRespDto(
    List<GetCategoryListRespDto> categories, //카테고리 리스트
    int currentPage,
    int totalPage,
    int totalElements,
    int pageSize
) {

}
