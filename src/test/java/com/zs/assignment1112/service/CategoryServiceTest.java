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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        when(categoryRepository.findAll())
                .thenReturn(List.of(category));

        List<CategoryResponse> result =
                categoryService.getAllCategories();

        assertThat(result).hasSize(1);

        CategoryResponse response = result.get(0);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Electronics");

        verify(categoryRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenNoCategoriesExist() {

        when(categoryRepository.findAll())
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getAllCategories()
        );

        assertThat(exception.getMessage())
                .isEqualTo("No categories available");

        verify(categoryRepository).findAll();
    }
}
