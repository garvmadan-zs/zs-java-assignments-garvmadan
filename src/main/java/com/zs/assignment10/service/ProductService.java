package com.zs.assignment10.service;
import java.util.List;

import com.zs.assignment10.DAO.DaoImplementation;
import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;

public class ProductService {
    private final DaoImplementation daoImplementation;
    public ProductService(DaoImplementation daoImplementation){
        this.daoImplementation=daoImplementation;
    }

    public List<Product> findAll(){
        return daoImplementation.findAll();
    }
    public Product findById(int id){
        try {
            return daoImplementation.findById(id);
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertProduct(Product product){
            daoImplementation.insertProduct(product);
    }
    public void updateName(int id, String name){
            daoImplementation.updateName(id,name);
    }
    public void updatePrice(int id, double price){
        daoImplementation.updatePrice(id,price);
    }
    public void updateQuantity(int id, int quantity){
        daoImplementation.updateQuantity(id,quantity);
    }
    public void deleteProduct(int id){
        daoImplementation.deleteProduct(id);
    }
    public boolean exists(int id){
        try {
            return   daoImplementation.exists(id);
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
