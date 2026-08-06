package com.zs.assignment1112.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Response DTO for Product.
 */
@Data
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Long categoryId;

    private String categoryName;

    public ProductResponse(Long id, String name, BigDecimal price, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
