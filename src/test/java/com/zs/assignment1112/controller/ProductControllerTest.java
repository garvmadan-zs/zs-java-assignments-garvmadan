package com.zs.assignment1112.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;
import com.zs.assignment1112.exception.ResourceNotFoundException;
import com.zs.assignment1112.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProducts() throws Exception {

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(50000))
                .categoryId(1L)
                .categoryName("Electronics")
                .build();

        when(productService.getAllProducts())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(50000))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));
    }

    @Test
    void shouldReturnProductsByCategory() throws Exception {

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("Spring Boot Book")
                .price(BigDecimal.valueOf(500))
                .categoryId(1L)
                .categoryName("Books")
                .build();

        when(productService.getProductsByCategory(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spring Boot Book"))
                .andExpect(jsonPath("$[0].categoryName").value("Books"));
    }

    @Test
    void shouldCreateProduct() throws Exception {

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("MacBook Pro")
                .price(BigDecimal.valueOf(150000))
                .categoryName("Electronics")
                .build();

        when(productService.createProduct(any(CreateProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"MacBook Pro",
                                  "price":150000,
                                  "categoryId":1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Product-Created", "true"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("MacBook Pro"))
                .andExpect(jsonPath("$.price").value(150000))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(productService).createProduct(any(CreateProductRequest.class));
    }

    @Test
    void shouldUpdateProduct() throws Exception {

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("MacBook Air")
                .price(BigDecimal.valueOf(120000))
                .categoryName("Electronics")
                .build();

        when(productService.updateProduct(eq(1L), any(UpdateProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"MacBook Air",
                                  "price":120000,
                                  "categoryId":1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("MacBook Air"))
                .andExpect(jsonPath("$.price").value(120000))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(productService).updateProduct(eq(1L), any(UpdateProductRequest.class));
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {

        when(productService.getProductsByCategory(100L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "No products found for category id: 100"
                        )
                );

        mockMvc.perform(get("/products/category/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("No products found for category id: 100"));
    }

    @Test
    void shouldReturn400WhenIllegalArgumentExceptionOccurs() throws Exception {

        when(productService.getProductsByCategory(-1L))
                .thenThrow(
                        new IllegalArgumentException(
                                "Category id must be positive"
                        )
                );

        mockMvc.perform(get("/products/category/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Category id must be positive"));
    }

    @Test
    void shouldReturn500WhenUnexpectedExceptionOccurs() throws Exception {

        when(productService.getProductsByCategory(1L))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/products/category/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message")
                        .value("An unexpected error occurred"));
    }

    @Test
    void shouldReturn400WhenCreateProductRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"",
                                    "price":-100,
                                    "categoryId":null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name")
                        .value("Product name is required"))
                .andExpect(jsonPath("$.price")
                        .value("Price must be positive"))
                .andExpect(jsonPath("$.categoryId")
                        .value("Category id is required"));
    }
}
