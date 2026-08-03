package com.zs.assignment1112.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;


    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;


    @NotNull(message = "Category id is required")
    private Long categoryId;
}
