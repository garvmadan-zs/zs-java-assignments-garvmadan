package controller;

import model.*;
import services.CategoryService;
import services.ProductService;

import java.util.List;
import java.util.Scanner;

public class AdminController {
    private final Scanner scanner;
    private final CategoryService categoryService;
    private final ProductService productService;

    public AdminController(Scanner scanner, CategoryService categoryService, ProductService productService) {
        this.scanner = scanner;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View Categories");
            System.out.println("2. View Subcategories");
            System.out.println("3. Add Category");
            System.out.println("4. Add Subcategory");
            System.out.println("5. Add Product");
            System.out.println("6. View Products");
            System.out.println("7. Search Product");
            System.out.println("8. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewCategories();
                    break;
                case "2":
                    viewSubCategories();
                    break;
                case "3":
                    addCategory();
                    break;
                case "4":
                    addSubCategory();
                    break;
                case "5":
                    addProduct();
                    break;
                case "6":
                    viewProducts();
                    break;
                case "7":
                    searchProducts();
                    break;
                case "8":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewCategories() {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("\nCategories:");
        for (Category category : categories) {
            System.out.println("- " + category.getName() + " : " + category.getDescription());
        }
    }

    private void viewSubCategories() {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        Category category = categoryService.findCategory(categoryName);
        if (category == null) {
            System.out.println("Category not found.");
            return;
        }
        System.out.println("\nSubcategories for " + category.getName() + ":");
        for (SubCategory subCategory : category.getSubCategories()) {
            System.out.println("- " + subCategory.getName() + " : " + subCategory.getDescription());
        }
    }

    private void addCategory() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        categoryService.addCategory(name, description);
        System.out.println("Category added.");
    }

    private void addSubCategory() {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        System.out.print("Enter subcategory name: ");
        String subName = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        SubCategory subCategory = categoryService.addSubCategory(categoryName, subName, description);
        if (subCategory != null) {
            System.out.println("Subcategory added.");
        } else {
            System.out.println("Category not found.");
        }
    }

    private void addProduct() {
        System.out.print("Enter product type (Grocery/Electronics/Personal Care): ");
        String type = scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter SKU: ");
        String sku = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter stock quantity: ");
        int stock = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter subcategory: ");
        String subCategory = scanner.nextLine();

        Product product;
        switch (type.toLowerCase()) {
            case "grocery":
                System.out.print("Enter expiry date: ");
                String expiry = scanner.nextLine();
                product = new GroceryProduct(productService.getAllProducts().size() + 1, sku, name, price, brand, stock, description, category, subCategory, expiry);
                break;
            case "electronics":
                System.out.print("Enter warranty period: ");
                String warranty = scanner.nextLine();
                product = new ElectronicsProduct(productService.getAllProducts().size() + 1, sku, name, price, brand, stock, description, category, subCategory, warranty);
                break;
            case "personal care":
                System.out.print("Enter skin type: ");
                String skinType = scanner.nextLine();
                product = new PersonalCareProduct(productService.getAllProducts().size() + 1, sku, name, price, brand, stock, description, category, subCategory, skinType);
                break;
            default:
                System.out.println("Invalid product type.");
                return;
        }

        productService.addProduct(product);
        System.out.println("Product added successfully.");
    }

    private void viewProducts() {
        List<Product> products = productService.getAllProducts();
        System.out.println("\nProducts:");
        for (Product product : products) {
            System.out.println(product.getDisplayDetails());
        }
    }

    private void searchProducts() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        List<Product> results = productService.searchProducts(keyword);
        if (results.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Product product : results) {
                System.out.println(product.getDisplayDetails());
            }
        }
    }
}
