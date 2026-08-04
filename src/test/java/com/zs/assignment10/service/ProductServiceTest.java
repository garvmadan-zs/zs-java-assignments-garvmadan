package com.zs.assignment10.service;

import com.zs.assignment10.DAO.DaoInterface;
import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import  org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
class ProductServiceTest {
    private DaoInterface dao;
    private ProductService service;
    @BeforeEach
    void setup(){
        dao = Mockito.mock(DaoInterface.class);
        service = new ProductService(dao);
    }
    @Test
    void shouldReturnAllProducts(){
        List<Product> products =
                List.of(new Product("Laptop",1,50000,2));
        Mockito.when(dao.findAll()).thenReturn(products);
        List<Product> result = service.findAll();
        Assertions.assertEquals(1,result.size());
        Mockito.verify(dao).findAll();
    }
    @Test
    void shouldReturnProductById() throws ProductNotFoundException {
        Product product = new Product("Laptop",1,50000,2);
        Mockito.when(dao.findById(1)).thenReturn(product);
        Product result = service.findById(1);
        Assertions.assertEquals("Laptop",
                result.getName());}
    @Test
    void shouldThrowExceptionWhenProductNotFound() throws ProductNotFoundException {
        Mockito.when(dao.findById(10)).thenThrow(new ProductNotFoundException("Product not found"));
        Assertions.assertThrows(
                ProductNotFoundException.class,
                () -> service.findById(10));}
    @Test
    void shouldInsertProduct(){
        Product product = new Product("Phone", 2, 20000, 5);
        service.insertProduct(product);
        Mockito.verify(dao).insertProduct(product);}
    @Test
    void shouldDeleteProduct() throws ProductNotFoundException {
        service.deleteProduct(1);
        Mockito.verify(dao).deleteProduct(1);}
    @Test
    void shouldCheckProductExists() throws Exception {
        Mockito.when(dao.exists(1)).thenReturn(true);
        boolean result = service.exists(1);
        Assertions.assertTrue(result);
    }
}
