package com.zs.assignment1112.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Response DTO for Category.
 */
@Data
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
}

