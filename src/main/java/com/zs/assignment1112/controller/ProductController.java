package com.zs.assignment1112.controller;


import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;
import com.zs.assignment1112.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST controller responsible for product related APIs.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log =
            LogManager.getLogger(ProductController.class);


    private final ProductService productService;

    /**
     * Fetches all products.
     *
     * @return list of products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {


        log.info("Request received to fetch all products");


        List<ProductResponse> products =
                productService.getAllProducts();


        return ResponseEntity.ok(products);
    }

    /**
     * Fetches products belonging to a category.
     *
     * @param categoryId category identifier
     * @return products belonging to category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId
    ) {


        log.info(
                "Request received for products of category {}",
                categoryId
        );


        List<ProductResponse> products =
                productService.getProductsByCategory(categoryId);


        return ResponseEntity.ok(products);

    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        System.out.println("CREATE PRODUCT API HIT");
        log.info(
                "Received request to create product: {}",
                request.getName()
        );


        ProductResponse response =
                productService.createProduct(request);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Product-Created", "true")
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {

        log.info(
                "Received request to update product with id: {}",
                id
        );
        ProductResponse response =
                productService.updateProduct(
                        id,
                        request
                );
        return ResponseEntity.ok()
                .header("Product-updated", "true")
                .body(response);
    }

}

