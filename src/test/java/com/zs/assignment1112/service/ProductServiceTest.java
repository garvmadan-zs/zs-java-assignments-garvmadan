package com.zs.assignment1112.service;

import com.zs.assignment1112.dto.request.CreateProductRequest;
import com.zs.assignment1112.dto.request.UpdateProductRequest;
import com.zs.assignment1112.dto.response.ProductResponse;
import com.zs.assignment1112.entity.Category;
import com.zs.assignment1112.entity.Product;
import com.zs.assignment1112.exception.ResourceNotFoundException;
import com.zs.assignment1112.repository.CategoryRepository;
import com.zs.assignment1112.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllProducts() {

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(50000))
                .category(category)
                .build();

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        List<ProductResponse> result = productService.getAllProducts();

        assertThat(result).hasSize(1);

        ProductResponse response = result.get(0);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop");
        assertThat(response.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(50000));
        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getCategoryName()).isEqualTo("Electronics");

        verify(productRepository).findAll();
    }

    @Test
    void shouldReturnProductsByCategory() {

        Long categoryId = 1L;

        Category category = Category.builder()
                .id(categoryId)
                .name("Books")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Spring Boot Book")
                .price(BigDecimal.valueOf(500))
                .category(category)
                .build();

        when(productRepository.findByCategoryId(categoryId))
                .thenReturn(List.of(product));

        List<ProductResponse> result =
                productService.getProductsByCategory(categoryId);

        assertThat(result).hasSize(1);

        ProductResponse response = result.get(0);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Spring Boot Book");
        assertThat(response.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(500));
        assertThat(response.getCategoryId()).isEqualTo(categoryId);
        assertThat(response.getCategoryName()).isEqualTo("Books");

        verify(productRepository).findByCategoryId(categoryId);
    }

    @Test
    void shouldThrowExceptionWhenNoProductsExist() {

        when(productRepository.findAll())
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getAllProducts()
        );

        assertThat(exception.getMessage())
                .isEqualTo("No products available");

        verify(productRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenNoProductsExistForCategory() {

        Long categoryId = 10L;

        when(productRepository.findByCategoryId(categoryId))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductsByCategory(categoryId)
        );

        assertThat(exception.getMessage())
                .isEqualTo("No products found for category id: 10");

        verify(productRepository).findByCategoryId(categoryId);
    }

    @Test
    void shouldUpdateProduct() {

        Long productId = 1L;


        Category category =
                Category.builder()
                        .id(1L)
                        .name("Books")
                        .build();


        Product existingProduct =
                Product.builder()
                        .id(productId)
                        .name("Old Name")
                        .price(BigDecimal.valueOf(200))
                        .category(category)
                        .build();


        UpdateProductRequest request =
                UpdateProductRequest.builder()
                        .name("Updated Book")
                        .price(BigDecimal.valueOf(500))
                        .categoryId(1L)
                        .build();


        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));


        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));


        when(productRepository.save(any(Product.class)))
                .thenReturn(existingProduct);


        ProductResponse response =
                productService.updateProduct(
                        productId,
                        request
                );


        assertThat(response.getName())
                .isEqualTo("Updated Book");


        verify(productRepository)
                .findById(productId);


        verify(categoryRepository)
                .findById(1L);


        verify(productRepository)
                .save(existingProduct);

    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {

        when(productRepository.findById(10L))
                .thenReturn(Optional.empty());


        UpdateProductRequest request =
                UpdateProductRequest.builder()
                        .name("Phone")
                        .price(BigDecimal.valueOf(1000))
                        .categoryId(1L)
                        .build();


        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(10L, request)
        );


        verify(productRepository)
                .findById(10L);

    }

    @Test
    void shouldCreateProduct() {

        CreateProductRequest request = new CreateProductRequest();
        request.setName("MacBook Pro");
        request.setPrice(BigDecimal.valueOf(150000));
        request.setCategoryId(1L);

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("MacBook Pro")
                .price(BigDecimal.valueOf(150000))
                .category(category)
                .build();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        ProductResponse response =
                productService.createProduct(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("MacBook Pro");
        assertThat(response.getCategoryName()).isEqualTo("Electronics");

        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
}
