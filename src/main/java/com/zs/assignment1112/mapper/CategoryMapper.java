package com.zs.assignment1112.mapper;

import com.zs.assignment1112.dto.response.CategoryResponse;
import com.zs.assignment1112.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
