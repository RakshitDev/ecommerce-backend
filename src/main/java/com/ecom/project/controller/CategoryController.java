package com.ecom.project.controller;

import com.ecom.project.config.AppConstant;
import com.ecom.project.config.ResponseStructure;
import com.ecom.project.payload.CategoryDto;
import com.ecom.project.payload.CategoryResponse;
import com.ecom.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/category")
    public ResponseEntity<ResponseStructure<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }
    @GetMapping("public/category")
    ResponseEntity<ResponseStructure<CategoryResponse>> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstant.SORT_CATEGORY_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORT_DIR,required = false) String sortOrder
    ) {
        return categoryService.findAllCategory(pageNumber,pageSize,sortBy,sortOrder);
    }
    @PutMapping("/admin/category/{categoryId}")
    ResponseEntity<ResponseStructure<CategoryDto>> updateCategory(@PathVariable("categoryId") Long categoryId,
                                                                  @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryId,categoryDto);
    }
    @DeleteMapping("admin/category/{categoryId}")
    ResponseEntity<ResponseStructure<CategoryDto>> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
