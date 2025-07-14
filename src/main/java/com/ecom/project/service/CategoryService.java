package com.ecom.project.service;

import com.ecom.project.config.ResponseStructure;
import com.ecom.project.payload.CategoryDto;
import com.ecom.project.payload.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<ResponseStructure<CategoryDto>> createCategory(CategoryDto categoryDto);

    ResponseEntity<ResponseStructure<CategoryResponse>> findAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseEntity<ResponseStructure<CategoryDto>> updateCategory(Long categoryId, @Valid CategoryDto categoryDto);

    ResponseEntity<ResponseStructure<CategoryDto>> deleteCategory(Long categoryId);
}
