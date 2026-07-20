package com.zs.assignment3.services;

import com.zs.assignment3.model.*;
import com.zs.assignment3.services.*;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final CategoryService categoryService;

    public ProductService(CategoryService categoryService) {
        this.categoryService = categoryService;
        seedProducts();
    }

    private void seedProducts() {
        addProduct(new GroceryProduct(1, "GR-1001", "Apples", 2.50, "FreshFarm", 50, "Fresh red apples", "Grocery", "Fruits", "2026-08-10"));
        addProduct(new GroceryProduct(2, "GR-1002", "Spinach", 1.80, "GreenField", 40, "Packed spinach", "Grocery", "Vegetables", "2026-07-20"));
        addProduct(new ElectronicsProduct(3, "EL-2001", "Smartphone", 699.99, "TechPlus", 20, "Android smartphone", "Electronics", "Mobile", "1 year"));
        addProduct(new ElectronicsProduct(4, "EL-2002", "Laptop", 1299.99, "ByteTech", 15, "14 inch laptop", "Electronics", "Laptops", "2 years"));
        addProduct(new PersonalCareProduct(5, "PC-3001", "Face Cream", 18.50, "GlowCo", 30, "Moisturizer", "Personal Care", "Skincare", "Dry"));
        addProduct(new PersonalCareProduct(6, "PC-3002", "Shampoo", 9.99, "PureCare", 25, "Sulfate free", "Personal Care", "Haircare", "All"));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product addProduct(Product product) {
        products.add(product);
        return product;
    }

    public Product findById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        String search = keyword.toLowerCase();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(search)
                    || product.getBrand().toLowerCase().contains(search)
                    || product.getCategory().toLowerCase().contains(search)
                    || product.getSubCategory().toLowerCase().contains(search)) {
                results.add(product);
            }
        }
        return results;
    }

    public List<Product> getProductsByCategory(String categoryName) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(categoryName)) {
                results.add(product);
            }
        }
        return results;
    }

    public List<Product> getProductsBySubCategory(String categoryName, String subCategoryName) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(categoryName)
                    && product.getSubCategory().equalsIgnoreCase(subCategoryName)) {
                results.add(product);
            }
        }
        return results;
    }
}
