package com.zs.assignment1112.controller;

import com.zs.assignment1112.dto.response.CategoryResponse;
import com.zs.assignment1112.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryController = new CategoryController(categoryService);
    }


    @Test
    void getAllCategories_ShouldReturnCategories_WhenCategoriesExist() {

        List<CategoryResponse> categories = List.of(new CategoryResponse(1L, "Electronics"));

        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);


        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllCategories();


        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());
        Assertions.assertEquals("Electronics", response.getBody().get(0).getName());

        Mockito.verify(categoryService).getAllCategories();
    }


    @Test
    void getAllCategories_ShouldReturnEmptyList_WhenNoCategoriesExist() {

        Mockito.when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());


        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllCategories();


        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isEmpty());

        Mockito.verify(categoryService).getAllCategories();
    }


    @Test
    void getAllCategories_ShouldPropagateException_WhenServiceFails() {

        Mockito.when(categoryService.getAllCategories()).thenThrow(new RuntimeException("Database error"));


        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> categoryController.getAllCategories());


        Assertions.assertEquals("Database error", exception.getMessage());

        Mockito.verify(categoryService).getAllCategories();
    }
}
