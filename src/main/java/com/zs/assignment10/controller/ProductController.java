package com.zs.assignment10.controller;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.service.ProductService;
import com.zs.assignment4.controller.Menu;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    private final ProductService productService;
    private static final Logger logger= LoggerFactory.getLogger(ProductController.class);
    Scanner sc= new Scanner(System.in);

    public ProductController(ProductService productService){
        this.productService=productService;
    }
    public void findAll(){
        System.out.println(productService.findAll());
    }
    public void findById(){
        System.out.print("Enter the id : ");
        int id=sc.nextInt();
        System.out.println(productService.findById(id));
    }
    public void insertProduct(){
        System.out.println("Enter the id : ");
        int id=sc.nextInt();
        System.out.println("Enter the name : ");
        String name=sc.next();
        System.out.println("Enter the price : ");
        double price=sc.nextDouble();
        System.out.println("Enter the quantity : ");
        int quantity=sc.nextInt();
        productService.insertProduct(new Product(name,id,price,quantity));

    }
    public void updateProduct(){
        System.out.println("Enter the product id : ");
        int id=sc.nextInt();
        System.out.println("Enter the new name : ");
        String name=sc.next();
        System.out.println("Enter the new price : ");
        double price=sc.nextDouble();
        System.out.println("Enter the new quantity : ");
        int quantity=sc.nextInt();
        productService.updateName(id,name);
        productService.updatePrice(id,price);
        productService.updateQuantity(id,quantity);
    }
    public void deleteProduct(){
        System.out.println("Enter the product id : ");
        int id=sc.nextInt();
        productService.deleteProduct(id);
    }
    public void exists(){
        System.out.println("Enter the product id : ");
        int id=sc.nextInt();
        System.out.println(productService.exists(id));
    }
}
