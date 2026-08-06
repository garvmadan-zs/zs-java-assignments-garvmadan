package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.response.CategoryResponse;
import com.zs.assignment1112.entity.Category;
import com.zs.assignment1112.exception.ResourceNotFoundException;
import com.zs.assignment1112.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllCategories() {

        Category category = Category.builder().id(1L).name("Electronics").build();

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponse> result = categoryService.getAllCategories();

        Assertions.assertThat(result).hasSize(1);

        CategoryResponse response = result.get(0);

        Assertions.assertThat(response.getId()).isEqualTo(1L);
        Assertions.assertThat(response.getName()).isEqualTo("Electronics");

        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenNoCategoriesExist() {

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of());

        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.getAllCategories());

        Assertions.assertThat(exception.getMessage()).isEqualTo("No categories available");

        Mockito.verify(categoryRepository).findAll();
    }
}
