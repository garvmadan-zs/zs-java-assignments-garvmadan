package com.zs.assignment1112.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for Product entity.
 */
class ProductEntityTest {
    @Test
    void shouldCreateProduct() {


        Product product =
                Product.builder()
                        .id(1L)
                        .name("Laptop")
                        .price(BigDecimal.valueOf(80000))
                        .build();


        assertThat(product.getId())
                .isEqualTo(1L);


        assertThat(product.getName())
                .isEqualTo("Laptop");


        assertThat(product.getPrice())
                .isEqualByComparingTo("80000");

    }

    @Test
    void shouldAssignCategoryToProduct() {
        Category category =
                Category.builder()
                        .name("Electronics")
                        .build();
        Product product =
                Product.builder()
                        .name("Phone")
                        .price(BigDecimal.valueOf(50000))
                        .category(category)
                        .build();
        assertThat(product.getCategory())
                .isEqualTo(category);

    }
}
