package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;
import com.zs.assignment1112.entity.Category;
import com.zs.assignment1112.entity.Product;
import com.zs.assignment1112.exception.ResourceNotFoundException;
import com.zs.assignment1112.repository.CategoryRepository;
import com.zs.assignment1112.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldReturnAllProducts() {

        Category category = Category.builder().id(1L).name("Electronics").build();


        Product product = Product.builder().id(1L).name("Laptop").price(BigDecimal.valueOf(50000)).category(category).build();


        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));


        List<ProductResponse> result = productService.getAllProducts();


        Assertions.assertThat(result).hasSize(1);


        ProductResponse response = result.get(0);


        Assertions.assertThat(response.getId()).isEqualTo(1L);

        Assertions.assertThat(response.getName()).isEqualTo("Laptop");

        Assertions.assertThat(response.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(50000));

        Assertions.assertThat(response.getCategoryId()).isEqualTo(1L);

        Assertions.assertThat(response.getCategoryName()).isEqualTo("Electronics");


        Mockito.verify(productRepository).findAll();
    }


    @Test
    void shouldReturnProductsByCategory() {

        Long categoryId = 1L;


        Category category = Category.builder().id(categoryId).name("Books").build();


        Product product = Product.builder().id(1L).name("Spring Boot Book").price(BigDecimal.valueOf(500)).category(category).build();


        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));


        Mockito.when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of(product));


        List<ProductResponse> result = productService.getProductsByCategory(categoryId);


        Assertions.assertThat(result).hasSize(1);


        ProductResponse response = result.get(0);


        Assertions.assertThat(response.getName()).isEqualTo("Spring Boot Book");

        Assertions.assertThat(response.getCategoryName()).isEqualTo("Books");


        Mockito.verify(categoryRepository).findById(categoryId);


        Mockito.verify(productRepository).findByCategoryId(categoryId);
    }


    @Test
    void shouldThrowExceptionWhenNoProductsExist() {


        Mockito.when(productRepository.findAll()).thenReturn(List.of());


        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.getAllProducts());


        Assertions.assertThat(exception.getMessage()).isEqualTo("No products available");


        Mockito.verify(productRepository).findAll();
    }


    @Test
    void shouldThrowExceptionWhenNoProductsExistForCategory() {


        Long categoryId = 10L;


        Category category = Category.builder().id(categoryId).name("Books").build();


        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));


        Mockito.when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of());


        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.getProductsByCategory(categoryId));


        Assertions.assertThat(exception.getMessage()).isEqualTo("No products found for category id: 10");


        Mockito.verify(categoryRepository).findById(categoryId);


        Mockito.verify(productRepository).findByCategoryId(categoryId);
    }


    @Test
    void shouldCreateProduct() {


        CreateProductRequest request = new CreateProductRequest();


        request.setName("MacBook Pro");
        request.setPrice(BigDecimal.valueOf(150000));
        request.setCategoryId(1L);


        Category category = Category.builder().id(1L).name("Electronics").build();


        Product savedProduct = Product.builder().id(1L).name("MacBook Pro").price(BigDecimal.valueOf(150000)).category(category).build();


        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));


        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);


        ProductResponse response = productService.createProduct(request);


        Assertions.assertThat(response.getId()).isEqualTo(1L);


        Assertions.assertThat(response.getCategoryName()).isEqualTo("Electronics");


        Mockito.verify(categoryRepository).findById(1L);


        Mockito.verify(productRepository).save(Mockito.any(Product.class));
    }


    @Test
    void shouldThrowExceptionWhenCreatingProductWithInvalidCategory() {


        CreateProductRequest request = new CreateProductRequest();


        request.setName("Laptop");
        request.setPrice(BigDecimal.valueOf(50000));
        request.setCategoryId(99L);


        Mockito.when(categoryRepository.findById(99L)).thenReturn(Optional.empty());


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(request));


        Mockito.verify(categoryRepository).findById(99L);


        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
    }


    @Test
    void shouldUpdateProduct() {


        Long productId = 1L;


        Category category = Category.builder().id(1L).name("Books").build();


        Product existingProduct = Product.builder().id(productId).name("Old Name").price(BigDecimal.valueOf(200)).category(category).build();


        UpdateProductRequest request = UpdateProductRequest.builder().name("Updated Book").price(BigDecimal.valueOf(500)).categoryId(1L).build();


        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));


        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));


        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(existingProduct);


        ProductResponse response = productService.updateProduct(productId, request);


        Assertions.assertThat(response.getName()).isEqualTo("Updated Book");


        Mockito.verify(productRepository).findById(productId);


        Mockito.verify(categoryRepository).findById(1L);


        Mockito.verify(productRepository).save(existingProduct);
    }


    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {


        Mockito.when(productRepository.findById(10L)).thenReturn(Optional.empty());


        UpdateProductRequest request = UpdateProductRequest.builder().name("Phone").price(BigDecimal.valueOf(1000)).categoryId(1L).build();


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(10L, request));


        Mockito.verify(productRepository).findById(10L);
    }


    @Test
    void shouldThrowExceptionWhenUpdatingProductWithInvalidCategory() {


        Long productId = 1L;


        Product product = Product.builder().id(productId).name("Laptop").price(BigDecimal.valueOf(50000)).build();


        UpdateProductRequest request = UpdateProductRequest.builder().name("Updated Laptop").price(BigDecimal.valueOf(60000)).categoryId(99L).build();


        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));


        Mockito.when(categoryRepository.findById(99L)).thenReturn(Optional.empty());


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, request));


        Mockito.verify(productRepository).findById(productId);


        Mockito.verify(categoryRepository).findById(99L);


        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
    }

}
