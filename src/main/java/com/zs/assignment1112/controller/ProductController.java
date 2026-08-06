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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

/**
 * REST controller responsible for product related APIs.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "API's for the Products")
public class ProductController {

    private static final Logger log = LogManager.getLogger(ProductController.class);


    private final ProductService productService;

    /**
     * Fetches all products.
     *
     * @return list of products
     */
    @Operation(summary = "Get all products available.")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Products fetched successfully."),

            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {


        log.info("Request received to fetch all products");


        List<ProductResponse> products = productService.getAllProducts();


        return ResponseEntity.ok(products);
    }

    /**
     * Fetches products belonging to a category.
     *
     * @param categoryId category identifier
     * @return products belonging to category
     */

    @Operation(summary = "Get products by category.")
    @GetMapping("/category/{categoryId}")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Products fetched successfully."),

            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {


        log.info("Request received for products of category {}", categoryId);


        List<ProductResponse> products = productService.getProductsByCategory(categoryId);


        return ResponseEntity.ok(products);

    }

    /**
     * Creates a new product.
     *
     * @param request request body containing product details
     * @return the newly created product
     */
    @Operation(summary = "Create a new product.")
    @PostMapping
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Product created successfully"),

            @ApiResponse(responseCode = "400", description = "Invalid product data"),

            @ApiResponse(responseCode = "500", description = "Internal server error")})

    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Received request to create product: {}", request.getName());


        ProductResponse response = productService.createProduct(request);


        return ResponseEntity.status(HttpStatus.CREATED).header("Product-Status", "Created").body(response);
    }


    /**
     * Updates an existing product.
     *
     * @param id      the identifier of the product to update
     * @param request request body containing updated product details
     * @return the updated product
     */
    @Operation(summary = "Update an existing product.")
    @PutMapping("/{id}")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Product updated successfully"),

            @ApiResponse(responseCode = "400", description = "Invalid product details"),

            @ApiResponse(responseCode = "404", description = "Product not found"),

            @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {

        log.info("Received request to update product with id: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok().header("Product-Status", "Updated").body(response);
    }

}

