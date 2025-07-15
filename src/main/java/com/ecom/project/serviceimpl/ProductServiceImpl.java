package com.ecom.project.serviceimpl;

import com.ecom.project.config.ResponseStructure;
import com.ecom.project.exception.ApiException;
import com.ecom.project.model.Category;
import com.ecom.project.model.Product;
import com.ecom.project.payload.ProductDto;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.repository.CategoryRepository;
import com.ecom.project.repository.ProductRepository;
import com.ecom.project.service.FileService;
import com.ecom.project.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    FileService fileService;
    @Value("{project.image}")
    String path;


    @Override
    public ResponseEntity<ResponseStructure<ProductDto>> createProduct(Long categoryId, ProductDto productDto) {
        //find category by id
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ApiException("Category with id " + categoryId + " not found"));
        //find product in category if present throw exception else add product
        Optional<Product> byProductName = productRepository.findByProductName(productDto.getProductName());
        if (byProductName.isPresent()) {
            throw new ApiException("Product with name " + productDto.getProductName() + " already exists");
        }
        //if not found map dto to entity and set related field
        Product product = modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);
        ResponseStructure<ProductDto> response = new ResponseStructure<>();
        response.setData(savedProductDto);
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Product created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseStructure<ProductResponse>> getAllProducts() {
        List<ProductDto> productDto = productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse response = new ProductResponse();
        response.setContent(productDto);
        ResponseStructure<ProductResponse> responseStructure = new ResponseStructure();
        responseStructure.setData(response);
        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Product list successfully");
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<ProductResponse>> getAllProductsByCategory(Long categoryId) {
        //find category by id
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException("Category with id " + categoryId + " not found"));
        //find category and map them to dto
        List<ProductDto> productDtos = productRepository.findByCategoryOrderByPriceAsc(category)
                .stream().map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse response = new ProductResponse();
        response.setContent(productDtos);
        ResponseStructure<ProductResponse> responseStructure = new ResponseStructure();
        responseStructure.setData(response);
        responseStructure.setMessage("List Of Products In categoryId : "+categoryId);
        responseStructure.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<ResponseStructure<ProductResponse>> getProductByKeyword(String keyword) {
        List<ProductDto> productDto = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%')
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        if(productDto.isEmpty()){
            throw new ApiException("Product with name " + keyword + " not found");
        }
        ProductResponse response = new ProductResponse();
        response.setContent(productDto);
        ResponseStructure<ProductResponse> responseStructure = new ResponseStructure();
        responseStructure.setData(response);
        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Product list ");

        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<ProductDto>> updateProductById(Long productId, ProductDto productDto) {
      //findById
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Product with id " + productId + " not found"));
       //update product
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setPrice(specialPrice);
        Product updatedProduct = productRepository.save(product);
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
       //add structure for product
        ResponseStructure<ProductDto> response = new ResponseStructure<>();
        response.setData(updatedProductDto);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Product updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<ProductDto>> deleteProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApiException("Product with id " + productId + " not found"));
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productRepository.delete(product);
        ResponseStructure<ProductDto> response = new ResponseStructure<>();
        response.setData(productDto);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<ProductDto>> updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.
                findById(productId)
                .orElseThrow(() -> new ApiException("Product with id " + productId + " not found"));
        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);
        Product updatedProduct = productRepository.save(product);
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        ResponseStructure<ProductDto> response = new ResponseStructure<>();
        response.setData(updatedProductDto);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Product updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
