package com.zs.assignment10.service;
import java.util.List;
import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.DAO.DaoInterface;
import com.zs.assignment10.model.Product;

public class ProductService {
    private final DaoInterface daoInterface;
    public ProductService(DaoInterface daoInterface){
        this.daoInterface=daoInterface;
    }

    public List<Product> findAll(){
        return daoInterface.findAll();
    }
    public Product findById(int id) throws ProductNotFoundException{

            return daoInterface.findById(id);

    }

    public void insertProduct(Product product){
            daoInterface.insertProduct(product);
    }

    public void updateProduct(int id, String name,double price,int quantity) throws ProductNotFoundException{

            daoInterface.updateProduct(id,name,price,quantity);

    }
    public void deleteProduct(int id) throws ProductNotFoundException{

            daoInterface.deleteProduct(id);

    }
    public boolean exists(int id) throws Exception {

            return   daoInterface.exists(id);

    }

}
