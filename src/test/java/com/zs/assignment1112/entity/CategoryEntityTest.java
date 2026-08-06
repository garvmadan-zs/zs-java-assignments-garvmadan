package com.zs.assignment1112.entity;

import org.junit.jupiter.api.Test;

import org.assertj.core.api.Assertions;

/**
 * Unit tests for Category entity.
 */
class CategoryEntityTest {
    @Test
    void shouldCreateCategory() {

        Category category = Category.builder().id(1L).name("Electronics").build();


        Assertions.assertThat(category.getId()).isEqualTo(1L);


        Assertions.assertThat(category.getName()).isEqualTo("Electronics");
    }


    @Test
    void shouldInitializeProductsList() {

        Category category = new Category();


        Assertions.assertThat(category.getProducts()).isNotNull();


        Assertions.assertThat(category.getProducts()).isEmpty();
    }

}
