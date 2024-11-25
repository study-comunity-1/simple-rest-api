package com.study.bookstore.domain.category.facade;

import com.study.bookstore.domain.category.dto.req.CreateCategoryReqDto;
import com.study.bookstore.domain.category.dto.resp.GetAllCategoryListRespDto;
import com.study.bookstore.domain.category.dto.resp.GetCategoryListRespDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import com.study.bookstore.domain.category.service.CreateCategoryService;
import com.study.bookstore.domain.category.service.DeleteCategoryService;
import com.study.bookstore.domain.category.service.ReadCategoryService;
import com.study.bookstore.domain.category.service.UpdateCategoryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class CategoryFacade {

  private final CreateCategoryService createCategoryService;
  private final UpdateCategoryService updateCategoryService;
  private final DeleteCategoryService deleteCategoryService;
  private final ReadCategoryService readCategoryService;


  //카테고리 추가
  public void addCategory(CreateCategoryReqDto req) {
    //1. 이름 조회 및 중복 체크
    boolean categoryNameExist = readCategoryService.existByCategoryName(req.categoryName());
    if (categoryNameExist) {
      throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
    }
    //2. 저장 서비스 호출
    Category category = req.of();
    createCategoryService.addCategory(category);
  }

  //카테고리 수정
  public void updateCategory(String categoryName, Long categoryId) {

    //1. 카테고리 아이디로 카테고리 존재 확인
    Category category = readCategoryService.findById(categoryId);
    //2. 다른 카테고리 중에 이름 중복 여부 확인
    boolean isDuplicated = readCategoryService.existsByCategoryNameAndCategoryIdNot(categoryName,
        categoryId);
    if (isDuplicated) {
      throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
    }
    //3. 수정 요청
    updateCategoryService.updateCategory(category, categoryName);
  }

  //카테고리 삭제
  public void deleteCategory(Long categoryId) {
    // 1. 카테고리 존재 여부 확인
    if (!readCategoryService.existByCategoryId(categoryId)) {
      throw new IllegalArgumentException("삭제하려는 카테고리 ID는 존재하지 않습니다.");
    }
    // 2. 카테고리 삭제
    deleteCategoryService.deleteCategory(categoryId);
  }
  //카테고리 상세조회
  public GetCategoryListRespDto getCategoryDetail(Long categoryId) {
    //1.카테고리 존재 여부 확인
    if (!readCategoryService.existByCategoryId(categoryId)) {
      throw new IllegalArgumentException("조회하려는 카테고리 ID는 존재하지 않습니다.");
    }
    //2.카테고리 상세 정보
    Category category = readCategoryService.findById(categoryId);
    //3.카테고리 정보 dto로 변환하여 반환
    return GetCategoryListRespDto.of(category);
  }

  //카테고리 전체 조회
  public GetAllCategoryListRespDto getCategories(int page, int size) {
    return readCategoryService.getAllCategories(page, size);
      }
}


