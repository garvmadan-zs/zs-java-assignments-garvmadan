package com.zs.assignment10.DAO;

import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;

import java.util.List;

public interface DaoInterface {
    void createProductTable();
    List<Product> findAll();
    Product findById(int id) throws ProductNotFoundException;
    void insertProduct(Product product);
    void updatePrice(int id,double price) throws ProductNotFoundException;;
    void updateName(int id,String name) throws ProductNotFoundException;;
    void updateQuantity(int id,int quantity) throws ProductNotFoundException;;
    void deleteProduct(int id);
    boolean exists(int id) throws ProductNotFoundException;
}
