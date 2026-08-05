package com.zs.assignment10.controller;

import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;
import com.zs.assignment10.service.ProductService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

class ProductControllerTest {
    private ProductService service;
    private ProductController controller;
    private ByteArrayOutputStream output;
    @BeforeEach
    void setup() {
        service = Mockito.mock(ProductService.class);
        controller = new ProductController(service);
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }
    @AfterEach
    void cleanup() {
        System.setOut(System.out);
    }
    @Test
    void shouldDisplayAllProducts() {
        Product product = new Product("Laptop", 1, 50000, 2);
        Mockito.when(service.findAll()).thenReturn(java.util.List.of(product));
        controller.findAll();
        Mockito.verify(service).findAll();
        Assertions.assertTrue(output.toString().contains("Laptop"));
    }
    @Test
    void shouldFindProductById() throws Exception {
        Product product = new Product("Phone", 2, 20000, 5);
        Mockito.when(service.findById(2)).thenReturn(product);
        Scanner scanner = new Scanner("2");
        controller.findById(scanner);
        Mockito.verify(service).findById(2);
        Assertions.assertTrue(output.toString().contains("Phone"));
    }
    @Test
    void shouldHandleProductNotFound() throws Exception {
       Mockito.when(service.findById(10)).thenThrow(new ProductNotFoundException("Product not found"));
        Scanner scanner = new Scanner("10");
       Assertions.assertDoesNotThrow(() -> controller.findById(scanner));
        Mockito.verify(service).findById(10);
    }
    @Test
    void shouldInsertProduct() {
        Scanner scanner = new Scanner("""
                1
                Laptop
                50000
                2
                """);
        controller.insertProduct(scanner);
        Mockito.verify(service).insertProduct(Mockito.any(Product.class));
    }
    @Test
    void shouldUpdateProduct() throws Exception {
        Scanner scanner = new Scanner("""
                1
                NewLaptop
                60000
                3
                """);
        controller.updateProduct(scanner);
        Mockito.verify(service).updateProduct(1, "NewLaptop", 60000, 3);}
    @Test
    void shouldHandleUpdateProductNotFound() throws Exception {
        Mockito.doThrow(new ProductNotFoundException("Not found")).when(service).updateProduct(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyInt());
        Scanner scanner = new Scanner("""
                1
                Laptop
                50000
                3
                """);
        Assertions.assertDoesNotThrow(() -> controller.updateProduct(scanner));
    }
    @Test
    void shouldDeleteProduct() throws Exception {
        Scanner scanner = new Scanner("5");
        controller.deleteProductMenu(scanner);
        Mockito.verify(service).deleteProduct(5);
    }
    @Test
    void shouldCheckProductExists() throws Exception {
        Mockito.when(service.exists(1)).thenReturn(true);
        Scanner scanner = new Scanner("1");
        controller.exists(scanner);
        Mockito.verify(service).exists(1);
        Assertions.assertTrue(output.toString().contains("true"));
    }
}
