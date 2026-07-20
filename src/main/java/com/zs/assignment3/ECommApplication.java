package com.zs.assignment3;


import com.zs.assignment3.services.CategoryService;
import com.zs.assignment3.services.ProductService;
import com.zs.assignment3.controller.AdminController;
import com.zs.assignment3.controller.CustomerController;
import java.util.Scanner;

public class ECommApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CategoryService categoryService = new CategoryService();
        ProductService productService = new ProductService(categoryService);
        AdminController adminController = new AdminController(scanner, categoryService, productService);
        CustomerController customerController = new CustomerController(scanner, categoryService, productService);

        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║    ECommerce Product Catalogue     ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("1. Admin Panel");
            System.out.println("2. Customer Panel");
            System.out.println("3.  Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    adminController.showMenu();
                    break;
                case "2":
                    customerController.showMenu();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
