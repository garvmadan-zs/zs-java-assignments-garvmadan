package com.zs.assignment1112.repository;

import com.zs.assignment1112.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for product database operations.
 */
@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {


    /**
     * Finds products belonging to a category.
     *
     * @param categoryId category identifier
     * @return products for the category
     */
    List<Product> findByCategoryId(Long categoryId);

}

