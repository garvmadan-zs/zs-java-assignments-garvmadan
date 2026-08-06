package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.response.CategoryResponse;
import com.zs.assignment1112.entity.Category;
import com.zs.assignment1112.exception.ResourceNotFoundException;
import com.zs.assignment1112.mapper.CategoryMapper;
import com.zs.assignment1112.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of category business operations.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger log =
            LogManager.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories =
                categoryRepository.findAll();
        if (categories.isEmpty()) {
          log.info("No category exits ");
        }
        log.debug(
                "Total categories fetched: {}",
                categories.size()
        );
        return categories.stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }
}
