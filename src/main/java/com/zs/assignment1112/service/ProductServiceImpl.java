package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;
import com.zs.assignment1112.entity.Category;
import com.zs.assignment1112.entity.Product;
import com.zs.assignment1112.exception.ResourceNotFoundException;
import com.zs.assignment1112.mapper.ProductMapper;
import com.zs.assignment1112.repository.CategoryRepository;
import com.zs.assignment1112.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of product related business operations.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log =
            LogManager.getLogger(ProductServiceImpl.class);


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products =
                productRepository.findAll();

        if (products.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No products available"
            );
        }

        log.debug(
                "Total products fetched: {}",
                products.size()
        );
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        log.info("Fetching products for category id: {}", categoryId);
        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + categoryId));

        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No products found for category id: " + categoryId);
        }

        log.debug(
                "Products found for category {}: {}",
                categoryId,
                products.size()
        );

        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse createProduct(
            CreateProductRequest request
    ) {
        log.info(
                "Creating product with name {}",
                request.getName()
        );
        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + request.getCategoryId()
                                )
                        );
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(category)
                .build();
        Product savedProduct =
                productRepository.save(product);
        log.info(
                "Product created successfully with id {}",
                savedProduct.getId()
        );
        ProductResponse response = ProductMapper.toResponse(savedProduct);
        log.info("Response created successfully {}", response);

        return response;
    }

    @Override
    public ProductResponse updateProduct(
            Long id,
            UpdateProductRequest request
    ) {
        log.info(
                "Updating product with id: {}",
                id
        );
        Product product =
                productRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Product not found with id: " + id
                                )
                        );
        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + request.getCategoryId()
                                )
                        );
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        Product updatedProduct =
                productRepository.save(product);
        log.info(
                "Product updated successfully with id: {}",
                updatedProduct.getId()
        );
        return ProductMapper.toResponse(updatedProduct);
    }
}
