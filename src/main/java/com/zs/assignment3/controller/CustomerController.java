package controller;

import model.Category;
import model.Product;
import model.SubCategory;
import services.CategoryService;
import services.ProductService;
import java.util.List;
import java.util.Scanner;

public class CustomerController {
    private final Scanner scanner;
    private final CategoryService categoryService;
    private final ProductService productService;

    public CustomerController(Scanner scanner, CategoryService categoryService, ProductService productService) {
        this.scanner = scanner;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      Customer Panel          ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.println("1.  View Categories");
            System.out.println("2. View Subcategories");
            System.out.println("3. View Products by Category");
            System.out.println("4. View Products by Subcategory");
            System.out.println("5. View Product");
            System.out.println("6. Search Product");
            System.out.println("7. Back");
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
                    viewProductsByCategory();
                    break;
                case "4":
                    viewProductsBySubCategory();
                    break;
                case "5":
                    viewProduct();
                    break;
                case "6":
                    searchProducts();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewCategories() {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("\n Available Categories ");
        for (Category category : categories) {
            System.out.println("   • " + category.getName());
        }
    }

    private void viewSubCategories() {
        printAvailableCategories();
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        Category category = categoryService.findCategory(categoryName);
        if (category == null) {
            System.out.println("Category not found. Please try one of the categories listed above.");
            return;
        }
        System.out.println("\n Subcategories in '" + category.getName() + "'");
        for (SubCategory subCategory : category.getSubCategories()) {
            System.out.println("   • " + subCategory.getName());
        }
    }

    private void viewProductsByCategory() {
        printAvailableCategories();
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        Category category = categoryService.findCategory(categoryName);
        if (category == null) {
            System.out.println("Category not found. Please try one of the categories listed above.");
            return;
        }
        List<Product> products = productService.getProductsByCategory(categoryName);
        if (products.isEmpty()) {
            System.out.println("No products found for that category.");
            return;
        }
        System.out.println("\n️ Products in '" + category.getName() + "'");
        for (Product product : products) {
            System.out.println(product.getDisplayDetails());
        }
    }

    private void viewProductsBySubCategory() {
        printAvailableCategories();
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        Category category = categoryService.findCategory(categoryName);
        if (category == null) {
            System.out.println("Category not found. Please try one of the categories listed above.");
            return;
        }
        System.out.println("\n Available subcategories in '" + category.getName() + "'");
        for (SubCategory subCategory : category.getSubCategories()) {
            System.out.println("   • " + subCategory.getName());
        }
        System.out.print("Enter subcategory name: ");
        String subCategoryName = scanner.nextLine();
        List<Product> products = productService.getProductsBySubCategory(categoryName, subCategoryName);
        if (products.isEmpty()) {
            System.out.println("No products found for that subcategory.");
            return;
        }
        System.out.println("\n Showing products for '" + category.getName() + "' > '" + subCategoryName + "'");
        for (Product product : products) {
            System.out.println(product.getDisplayDetails());
        }
    }

    private void viewProduct() {
        System.out.print("Enter product ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Product product = productService.findById(id);
        if (product == null) {
            System.out.println("Product not found.");
        } else {
            System.out.println("\n Product Details");
            System.out.println("Category: " + product.getCategory());
            System.out.println("Subcategory: " + product.getSubCategory());
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
            System.out.println("\n Search Results ");
            for (Product product : results) {
                System.out.println(product.getDisplayDetails());
            }
        }
    }

    private void printAvailableCategories() {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("\n Available Categories ");
        for (Category category : categories) {
            System.out.println("   • " + category.getName());
        }
    }
}
