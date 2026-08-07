package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;

import java.util.List;

/**
 * Service interface for product related operations.
 */
public interface ProductService {
    /**
     * Fetches all products.
     *
     * @return list of products
     */
    List<ProductResponse> getAllProducts();

    /**
     * Fetches products belonging to a category.
     *
     * @param categoryId category identifier
     * @return products under category
     */
    List<ProductResponse> getProductsByCategory(Long categoryId);

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(
            Long id,
            UpdateProductRequest request
    );
}
