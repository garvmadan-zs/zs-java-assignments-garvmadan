package com.zs.assignment1112.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for Category entity.
 */
class CategoryEntityTest {
    @Test
    void shouldCreateCategory() {

        Category category =
                Category.builder()
                        .id(1L)
                        .name("Electronics")
                        .build();


        assertThat(category.getId())
                .isEqualTo(1L);


        assertThat(category.getName())
                .isEqualTo("Electronics");
    }


    @Test
    void shouldInitializeProductsList() {

        Category category =
                new Category();


        assertThat(category.getProducts())
                .isNotNull();


        assertThat(category.getProducts())
                .isEmpty();
    }

    @Test
    void shouldAddProductToCategory() {


        Category category =
                Category.builder()
                        .name("Books")
                        .build();


        Product product =
                Product.builder()
                        .name("Spring Boot Book")
                        .build();


        category.addProduct(product);


        assertThat(category.getProducts())
                .contains(product);


        assertThat(product.getCategory())
                .isEqualTo(category);
    }
}
