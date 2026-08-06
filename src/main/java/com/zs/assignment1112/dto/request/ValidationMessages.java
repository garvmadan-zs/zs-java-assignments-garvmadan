package com.zs.assignment1112.dto.request;

public class ValidationMessages {
    private ValidationMessages() {}

    public static final String PRODUCT_NAME_REQUIRED =
            "Product name is required";

    public static final String PRICE_REQUIRED =
            "Price is required";

    public static final String PRICE_POSITIVE =
            "Price should be positive";


    public static final String CATEGORY_ID_REQUIRED =
            "Category id is required";
}
