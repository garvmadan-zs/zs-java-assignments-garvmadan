package com.zs.assignment1112.repository;

import com.zs.assignment1112.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.assertj.core.api.Assertions;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldSaveAndFindCategory() {
        Category category = Category.builder().name("Electronics").build();


        Category saved = repository.save(category);


        Assertions.assertThat(saved.getId()).isNotNull();


        Assertions.assertThat(repository.findById(saved.getId())).isPresent();

    }
}
