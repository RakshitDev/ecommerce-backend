package com.ecom.project.serviceimpl;

import com.ecom.project.exception.ApiException;
import com.ecom.project.model.Category;
import com.ecom.project.config.ResponseStructure;
import com.ecom.project.payload.CategoryDto;
import com.ecom.project.payload.CategoryResponse;
import com.ecom.project.repository.CategoryRepository;
import com.ecom.project.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<ResponseStructure<CategoryDto>> createCategory(CategoryDto categoryDto) {
        //find by name if exists throw exception else save it
        Optional<Category> category = categoryRepository.findByCategoryName(categoryDto.getCategoryName());
        if (category.isPresent()) {
            throw new ApiException("Category already present ");
        }

        //mapping dto to class and saving it
        Category mappedCategory = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(mappedCategory);

        //mapping back saved category to dto
        CategoryDto dtoResponse = modelMapper.map(savedCategory, CategoryDto.class);
        ResponseStructure<CategoryDto> responseStructure = new ResponseStructure<>();
        responseStructure.setStatus(HttpStatus.CREATED.value());
        responseStructure.setMessage("Category Saved Successfully");
        responseStructure.setData(dtoResponse);

        return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseStructure<CategoryResponse>> findAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=
                sortOrder.equalsIgnoreCase("asc")
                        ?Sort.by(sortBy).ascending()
                        :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails );

        if (categoryPage.isEmpty()) {
            throw new ApiException("Category Not Found");
        }
        List<CategoryDto> categoryDto = categoryPage.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
        CategoryResponse categoryResponse= new CategoryResponse();
        categoryResponse.setContent(categoryDto);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPage(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());


        ResponseStructure<CategoryResponse> responseStructure = new ResponseStructure<>();
        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("List of Categories");
        responseStructure.setData(categoryResponse);
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseStructure<CategoryDto>> updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException("Category Not Found"));

        Optional<Category> duplicate = categoryRepository.findByCategoryName(categoryDto.getCategoryName());
        if (duplicate.isPresent() && !duplicate.get().getCategoryId().equals(categoryId)) {
            throw new ApiException("Category Already Exists");
        }
        category.setCategoryName(categoryDto.getCategoryName());

        Category savedCategory = categoryRepository.save(category);
        CategoryDto savedDto = modelMapper.map(savedCategory, CategoryDto.class);
        ResponseStructure<CategoryDto> responseStructure = new ResponseStructure<>();
        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Category Saved Successfully");
        responseStructure.setData(savedDto);
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseStructure<CategoryDto>> deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException("Category Not Found"));
        CategoryDto dtoResponse = modelMapper.map(category, CategoryDto.class);
        categoryRepository.delete(category);
        ResponseStructure<CategoryDto> responseStructure = new ResponseStructure<>();
        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Category Deleted Successfully");
        responseStructure.setData(dtoResponse);
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }
}
