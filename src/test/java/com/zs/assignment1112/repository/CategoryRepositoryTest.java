package com.zs.assignment1112.repository;

import com.zs.assignment1112.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldSaveAndFindCategory() {
        Category category =
                Category.builder()
                        .name("Electronics")
                        .build();


        Category saved =
                repository.save(category);


        assertThat(saved.getId())
                .isNotNull();


        assertThat(repository.findById(saved.getId()))
                .isPresent();

    }
}
