package com.zs.assignment10.controller;

import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.Exception.DatabaseException;
import com.zs.assignment10.model.Product;
import com.zs.assignment10.service.ProductService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
    public void findById(Scanner sc){
        System.out.print("Enter the id : ");
        int id=Integer.parseInt(sc.nextLine().trim());
        try {
            System.out.println(productService.findById(id));
        } catch (ProductNotFoundException e) {
            logger.error(e.getMessage());
        }
        catch(DatabaseException e){
            logger.error("Database error ",e);
        }
    }
    public void insertProduct(Scanner sc){
        System.out.println("Enter the id : ");
        int id=Integer.parseInt(sc.nextLine().trim());
        System.out.println("Enter the name : ");
        String name=sc.nextLine().trim();
        System.out.println("Enter the price : ");
        double price=Double.parseDouble(sc.nextLine().trim());
        System.out.println("Enter the quantity : ");
        int quantity=Integer.parseInt(sc.nextLine().trim());
        productService.insertProduct(new Product(name,id,price,quantity));

    }
    public void updateProduct(Scanner sc){
        System.out.println("Enter the product id : ");
        int id=Integer.parseInt(sc.nextLine().trim());
        System.out.println("Enter the new name : ");
        String name=sc.nextLine().trim();
        System.out.println("Enter the new price : ");
        double price=Double.parseDouble(sc.nextLine().trim());
        System.out.println("Enter the new quantity : ");
        int quantity=Integer.parseInt(sc.nextLine().trim());
        try {
            productService.updateProduct(id,name,price,quantity);
        } catch (ProductNotFoundException e) {
            logger.error(e.getMessage());
        }
        catch(DatabaseException e){
            logger.error("Database error ",e);
        }

    }

    public void deleteProductMenu(Scanner sc){
        System.out.println("Enter the product id : ");
        int id=Integer.parseInt(sc.nextLine().trim());
        try {
            productService.deleteProduct(id);
        } catch (ProductNotFoundException e) {
            logger.error(e.getMessage());
        }
        catch(DatabaseException e){
            logger.error("Database error ",e);
        }
    }
    public void exists(Scanner sc){
        System.out.println("Enter the product id : ");
        int id=Integer.parseInt(sc.nextLine().trim());
        try {
            System.out.println(productService.exists(id));
        }
        catch(Exception e){
            logger.error("Database error ",e);
        }
    }
}
