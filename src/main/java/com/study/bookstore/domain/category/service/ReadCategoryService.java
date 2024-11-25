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

@Service
@RequiredArgsConstructor
public class ReadCategoryService {

  private final CategoryRepository categoryRepository;

  //카테고리 이름으로 카테고리 존재 확인
  public boolean existByCategoryName(String categoryName) {
    return categoryRepository.existsByCategoryName(categoryName);
  }
  //카테고리 아이디로  확인
  public Category findById(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. 카테고리 ID: " + categoryId));
  }
  //카테고리 아이디로 카테고리 존재 확인
  public boolean existByCategoryId(Long categoryId){
    return categoryRepository.existsById(categoryId);
  }
  //카테고리 이름 중복 여부 확인
  public boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Long categoryId) {
    return categoryRepository.existsByCategoryNameAndCategoryIdNot(categoryName, categoryId);
  }
  // 전체 카테고리 조회 및 DTO 변환
  public GetAllCategoryListRespDto getAllCategories(int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size);

    // 데이터베이스에서 페이지네이션된 카테고리 목록 가져오기
    Page<Category> categoryPage = categoryRepository.findAll(pageable);

    // DTO로 변환
    List<GetCategoryListRespDto> categoryDtos = categoryPage.stream()
        .map(category -> new GetCategoryListRespDto(category.getCategoryId(), category.getCategoryName()))
        .collect(Collectors.toList());

    // 결과를 반환
    return new GetAllCategoryListRespDto(
        categoryDtos,
        categoryPage.getNumber() + 1,
        categoryPage.getTotalPages(),
        categoryPage.getNumberOfElements(),
        categoryPage.getSize()
    );
  }
}
