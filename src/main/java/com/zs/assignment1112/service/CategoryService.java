package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.response.CategoryResponse;

import java.util.List;

/**
 * Service interface for category related operations.
 */
public interface CategoryService {
    /**
     * Fetches all available categories.
     *
     * @return list of categories
     */
    List<CategoryResponse> getAllCategories();
}
