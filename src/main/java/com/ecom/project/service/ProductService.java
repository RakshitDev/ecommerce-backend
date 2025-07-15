package com.ecom.project.service;

import com.ecom.project.config.ResponseStructure;
import com.ecom.project.payload.ProductDto;
import com.ecom.project.payload.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ResponseEntity<ResponseStructure<ProductDto>> createProduct(Long categoryId, ProductDto productDto);

    ResponseEntity<ResponseStructure<ProductResponse>> getAllProducts();

    ResponseEntity<ResponseStructure<ProductResponse>> getAllProductsByCategory(Long categoryId);

    ResponseEntity<ResponseStructure<ProductResponse>> getProductByKeyword(String keyword);

    ResponseEntity<ResponseStructure<ProductDto>> updateProductById(Long productId, @Valid ProductDto productDto);

    ResponseEntity<ResponseStructure<ProductDto>> deleteProductById(Long productId);

    ResponseEntity<ResponseStructure<ProductDto>> updateProductImage(Long productId, MultipartFile image) throws IOException;
}
