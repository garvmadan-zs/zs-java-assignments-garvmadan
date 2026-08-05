package com.zs.assignment10.DAO;

import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DaoImplementationTest {
    private DaoImplementation dao;
    @BeforeEach
    void setup() {
        dao = new DaoImplementation();
        dao.createProductTable();
    }
    @Test
    void shouldInsertProduct() {
        Product product = new Product("Laptop", 1, 50000, 2);
        dao.insertProduct(product);
        List<Product> products = dao.findAll();
        Assertions.assertEquals(1, products.size());
    }
    @Test
    void shouldFindProductById() throws ProductNotFoundException {
        Product product = new Product("Phone", 2, 20000, 3);
        dao.insertProduct(product);
        Product result = dao.findById(2);
        Assertions.assertEquals("Phone", result.getName());
    }
    @Test
    void shouldThrowExceptionForInvalidId() {
        Assertions.assertThrows(ProductNotFoundException.class, () -> dao.findById(99));
    }
    @Test
    void shouldDeleteProduct() throws ProductNotFoundException {
        dao.insertProduct(new Product("Tablet", 3, 30000, 1));
        dao.deleteProduct(3);
        Assertions.assertThrows(ProductNotFoundException.class, () -> dao.findById(3));
    }
    @Test
    void shouldCheckExistence() {
        dao.insertProduct(new Product("Mouse", 4, 1000, 10));
        Assertions.assertTrue(dao.exists(4));
        Assertions.assertFalse(dao.exists(99));
    }
}
