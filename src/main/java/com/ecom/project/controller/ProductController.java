package com.ecom.project.controller;

import com.ecom.project.config.ResponseStructure;
import com.ecom.project.payload.ProductDto;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ResponseStructure<ProductDto>> createProduct(@Valid  @PathVariable("categoryId") Long categoryId,
                                                                       @RequestBody ProductDto productDto){
       return productService.createProduct(categoryId,productDto);
    }
    @GetMapping("/public/products")
    public ResponseEntity<ResponseStructure<ProductResponse>> getAllProducts(){
      return  productService.getAllProducts();
    }
    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ResponseStructure<ProductResponse>> getAllProductsByCategory(@PathVariable("categoryId") Long categoryId){
        return productService.getAllProductsByCategory(categoryId);
    }
    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ResponseStructure<ProductResponse>> getAllProductsByKeyword(@PathVariable("keyword") String keyword){
        return productService.getProductByKeyword(keyword);
    }
    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ResponseStructure<ProductDto>> updateProduct(@PathVariable("productId") Long productId,@Valid  @RequestBody ProductDto productDto){
        return productService.updateProductById(productId,productDto);
    }
    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ResponseStructure<ProductDto>> deleteProduct(@PathVariable("productId") Long productId){
        return productService.deleteProductById(productId);
    }
    @PutMapping("/admin/Product/{productId}/image")
    public ResponseEntity<ResponseStructure<ProductDto>> updateImage(@PathVariable("productId") Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        return productService.updateProductImage(productId,image);
    }
}
