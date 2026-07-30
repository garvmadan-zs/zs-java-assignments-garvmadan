package com.zs.assignment10.Exception;

import com.zs.assignment10.model.Product;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String message){
        super(message);
    }
}
