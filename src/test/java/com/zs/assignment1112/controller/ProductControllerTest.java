package com.zs.assignment1112.controller;

import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;
import com.zs.assignment1112.exception.*;
import com.zs.assignment1112.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    private ProductController productController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
    }


    @Test
    void shouldReturnAllProducts() {

        ProductResponse product = ProductResponse.builder().id(1L).name("Laptop").price(BigDecimal.valueOf(50000)).categoryId(1L).categoryName("Electronics").build();


        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product));


        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();


        Assertions.assertEquals(200, response.getStatusCode().value());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertEquals(1, response.getBody().size());

        Assertions.assertEquals("Laptop", response.getBody().get(0).getName());


        Mockito.verify(productService).getAllProducts();
    }


    @Test
    void shouldReturnEmptyListWhenNoProductsExist() {


        Mockito.when(productService.getAllProducts()).thenReturn(Collections.emptyList());


        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();


        Assertions.assertEquals(200, response.getStatusCode().value());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertTrue(response.getBody().isEmpty());


        Mockito.verify(productService).getAllProducts();
    }


    @Test
    void shouldReturnProductsByCategory() {


        Long categoryId = 1L;


        ProductResponse product = ProductResponse.builder().id(1L).name("Spring Boot Book").price(BigDecimal.valueOf(500)).categoryId(categoryId).categoryName("Books").build();


        Mockito.when(productService.getProductsByCategory(categoryId)).thenReturn(List.of(product));


        ResponseEntity<List<ProductResponse>> response = productController.getProductsByCategory(categoryId);


        Assertions.assertEquals(200, response.getStatusCode().value());


        Assertions.assertNotNull(response.getBody());


        Assertions.assertEquals("Spring Boot Book", response.getBody().get(0).getName());


        Mockito.verify(productService).getProductsByCategory(categoryId);
    }


    @Test
    void shouldCreateProduct() {


        CreateProductRequest request = new CreateProductRequest();

        request.setName("MacBook Pro");


        ProductResponse product = ProductResponse.builder().id(1L).name("MacBook Pro").price(BigDecimal.valueOf(150000)).categoryId(1L).categoryName("Electronics").build();


        Mockito.when(productService.createProduct(request)).thenReturn(product);


        ResponseEntity<ProductResponse> response = productController.createProduct(request);


        Assertions.assertEquals(201, response.getStatusCode().value());


        Assertions.assertNotNull(response.getBody());


        Assertions.assertEquals("MacBook Pro", response.getBody().getName());


        Assertions.assertEquals("Created", response.getHeaders().getFirst("Product-Status"));

        Mockito.verify(productService).createProduct(request);
    }


    @Test
    void shouldUpdateProduct() {

        Long productId = 1L;

        UpdateProductRequest request = UpdateProductRequest.builder().name("MacBook Air").price(BigDecimal.valueOf(120000)).categoryId(1L).build();


        ProductResponse product = ProductResponse.builder().id(productId).name("MacBook Air").price(BigDecimal.valueOf(120000)).categoryId(1L).categoryName("Electronics").build();


        Mockito.when(productService.updateProduct(productId, request)).thenReturn(product);


        ResponseEntity<ProductResponse> response = productController.updateProduct(productId, request);


        Assertions.assertEquals(200, response.getStatusCode().value());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertEquals("MacBook Air", response.getBody().getName());

        Assertions.assertEquals("Updated", response.getHeaders().getFirst("Product-Status"));


        Mockito.verify(productService).updateProduct(productId, request);
    }


    @Test
    void shouldPropagateResourceNotFoundExceptionFromService() {


        Long categoryId = 100L;


        Mockito.when(productService.getProductsByCategory(categoryId)).thenThrow(new RuntimeException("Category not found"));


        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productController.getProductsByCategory(categoryId));


        Assertions.assertEquals("Category not found", exception.getMessage());


        Mockito.verify(productService).getProductsByCategory(categoryId);
    }


    @Test
    void shouldPropagateExceptionWhenCreateProductFails() {


        CreateProductRequest request = new CreateProductRequest();


        Mockito.when(productService.createProduct(request)).thenThrow(new RuntimeException("Database error"));


        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productController.createProduct(request));


        Assertions.assertEquals("Database error", exception.getMessage());


        Mockito.verify(productService).createProduct(request);
    }

    @Test
    void shouldPropagateResourceNotFoundExceptionWhenUpdatingProductDoesNotExist() {

        Long productId = 99L;

        UpdateProductRequest request = UpdateProductRequest.builder().name("MacBook Air").price(BigDecimal.valueOf(120000)).categoryId(1L).build();


        Mockito.when(productService.updateProduct(productId, request)).thenThrow(new ResourceNotFoundException("Product not found with id: " + productId));


        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> productController.updateProduct(productId, request));


        Assertions.assertEquals("Product not found with id: 99", exception.getMessage());


        Mockito.verify(productService).updateProduct(productId, request);
    }
}
