package com.zs.assignment10.controller;


import com.zs.assignment10.service.ProductService;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final ProductService productService;

    public Menu(Scanner scanner, ProductService productService){

        this.scanner = scanner;
        this.productService = productService;
    }
    public void showMenu(){
        System.out.println("=======Assignment 10 Menu========");
        System.out.println("1. Print All the Products ");
        System.out.println("2. Get the product by id ");
        System.out.println("3. Enter a new product ");
        System.out.println("4. Update the product ");
        System.out.println("5. Delete the product ");
        System.out.println("6. Check if the product exists");
        System.out.println("7. Exit ");
        System.out.println("Enter your choice");
    }
    public void choiceEntry(){


        ProductController productController =
                new ProductController(productService);
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        productController.findAll();
                        break;
                    case "2":
                        productController.findById(scanner);
                        break;
                    case "3":
                        productController.insertProduct(scanner);
                        break;
                    case "4":
                        productController.updateProduct(scanner);
                        break;
                    case "5":
                        productController.deleteProductMenu(scanner);
                        break;
                    case "6":
                        productController.exists(scanner);
                        break;
                    case "7" :
                        System.out.println("Exiting......");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Enter a valid input : " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Enter a valid input : " + ex.getMessage());
            }
        }
    }
}
