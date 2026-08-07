package com.zs.assignment1112.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;

/**
 * Unit tests for Product entity.
 */
class ProductEntityTest {
    @Test
    void shouldCreateProduct() {


        Product product = Product.builder().id(1L).name("Laptop").price(BigDecimal.valueOf(80000)).build();


        Assertions.assertThat(product.getId()).isEqualTo(1L);


        Assertions.assertThat(product.getName()).isEqualTo("Laptop");


        Assertions.assertThat(product.getPrice()).isEqualByComparingTo("80000");

    }

    @Test
    void shouldAssignCategoryToProduct() {
        Category category = Category.builder().name("Electronics").build();
        Product product = Product.builder().name("Phone").price(BigDecimal.valueOf(50000)).category(category).build();
        Assertions.assertThat(product.getCategory()).isEqualTo(category);

    }
}
