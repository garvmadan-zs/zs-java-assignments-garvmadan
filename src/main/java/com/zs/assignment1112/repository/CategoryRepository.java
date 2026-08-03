package com.zs.assignment1112.repository;

import com.zs.assignment1112.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for category database operations.
 */
@Repository
public interface CategoryRepository
        extends JpaRepository<Category, Long> {

}
