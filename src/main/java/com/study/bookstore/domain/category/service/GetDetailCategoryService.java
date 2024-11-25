package com.study.bookstore.domain.category.service;

import com.study.bookstore.domain.book.dto.resp.GetBookRespDto;
import com.study.bookstore.domain.category.dto.resp.GetCategoryListRespDto;
import com.study.bookstore.domain.category.entity.Category;
import com.study.bookstore.domain.category.entity.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GetDetailCategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Long categoryId){
      return categoryRepository.findById(categoryId)
          .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }
}
