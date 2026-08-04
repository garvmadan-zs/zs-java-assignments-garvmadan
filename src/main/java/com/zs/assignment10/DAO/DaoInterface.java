package com.zs.assignment10.DAO;

import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;

import java.util.List;

public interface DaoInterface {
    void createProductTable();
    List<Product> findAll();
    Product findById(int id) throws ProductNotFoundException;
    void insertProduct(Product product);
    void updateProduct(int id, String name,double price,int quantity) throws ProductNotFoundException;
    void deleteProduct(int id) throws ProductNotFoundException;
    boolean exists(int id);
}
