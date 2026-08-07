package com.zs.assignment1112.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UpdateProductRequest {

    @NotBlank(message = ValidationMessages.PRODUCT_NAME_REQUIRED)
    private String name;


    @NotNull(message = ValidationMessages.PRICE_REQUIRED)
    @Positive(message = ValidationMessages.PRICE_POSITIVE)
    private BigDecimal price;

    @NotNull(message = ValidationMessages.CATEGORY_ID_REQUIRED)
    private Long categoryId;


}
