package com.zs.assignment1112.repository;

import com.zs.assignment1112.entity.Category;
import com.zs.assignment1112.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldFindProductsByCategory() {
        Category category = Category.builder().name("Books").build();
        Category savedCategory = categoryRepository.save(category);
        Product product = Product.builder().name("Spring Boot Guide").price(BigDecimal.valueOf(500)).category(savedCategory).build();
        productRepository.save(product);
        List<Product> products = productRepository.findByCategoryId(savedCategory.getId());
        Assertions.assertThat(products).isNotEmpty();
    }
}
