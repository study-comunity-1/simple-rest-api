package com.study.bookstore.domain.category.service;

import com.study.bookstore.domain.category.dto.resp.GetAllCategoryListRespDto;
import com.study.bookstore.domain.category.dto.resp.GetCategoryListRespDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GetAllCategoryService {
  private final CategoryRepository categoryRepository;

  public GetAllCategoryListRespDto getCategories(int page, int size){
    //페이지네이션 처리
    Pageable pageable = PageRequest.of(page-1, size);
    //카테고리 목록 가져오기
    Page<Category> categoroyPage = categoryRepository.findAll(pageable);

    //카테고리 목록을 dto로 변환(categoryPage.stream()을 사용)
    List<GetCategoryListRespDto> categoryDtos = categoroyPage.stream()
        .map(category -> new GetCategoryListRespDto(category.getCategoryId(), category.getCategoryName()))
        .collect(Collectors.toList());

    return new GetAllCategoryListRespDto(
        categoryDtos,
        categoroyPage.getNumber()+1,
        categoroyPage.getTotalPages(),
        categoroyPage.getNumberOfElements(),
        categoroyPage.getSize()
    );
  }
}
