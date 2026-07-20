package com.zs.assignment4.services;

import com.zs.assignment3.model.*;
import com.zs.assignment4.model.LruCache;
import com.zs.assignment4.model.SearchResult;
import com.zs.assignment3.services.CategoryService;
import com.zs.assignment3.services.ProductService;
import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    private static final int CACHE_CAPACITY = 10;
    private  final LruCache<String, SearchResult> searchCache;
    private final List<Category> categories;
    private final List<Product> products;
    private final CategoryService catalogueCategoryService;
    private final ProductService catalogueProductService;

    public CatalogService() {
        this.searchCache = new LruCache<>(CACHE_CAPACITY);
        this.catalogueCategoryService = new CategoryService();
        this.catalogueProductService = new ProductService(catalogueCategoryService);
        this.categories = new ArrayList<>();
        this.products = new ArrayList<>();
        seedData();
    }

    private void seedData() {
        addCatalogueData();
        addAssignmentSpecificData();
    }

    private void addCatalogueData() {
        for (Category category : catalogueCategoryService.getAllCategories()) {
            addCategoryIfMissing(category);
        }
        for (Product product : catalogueProductService.getAllProducts()) {
            addProductIfMissing(product);
        }
    }

    private void addAssignmentSpecificData() {
        Category fashion = new Category(10, "Fashion", "Clothing and accessories");
        fashion.addSubCategory(new SubCategory(10, "Men", "Men's clothing"));
        fashion.addSubCategory(new SubCategory(11, "Women", "Women's clothing"));
        addCategoryIfMissing(fashion);

        Category home = new Category(11, "Home", "Furniture and décor");
        home.addSubCategory(new SubCategory(12, "Furniture", "Tables and chairs"));
        home.addSubCategory(new SubCategory(13, "Decor", "Home decoration"));
        addCategoryIfMissing(home);

        Category books = new Category(12, "Books", "Reading and learning");
        books.addSubCategory(new SubCategory(14, "Fiction", "Story books"));
        books.addSubCategory(new SubCategory(15, "Science", "Educational books"));
        addCategoryIfMissing(books);




        addProductIfMissing(new GroceryProduct(102, "GR-002", "Bread", 1.20, "BakeHouse", 80, "Soft loaf", "Grocery", "Bakery", "2026-07-20"));
        addProductIfMissing(new ElectronicsProduct(103, "EL-001", "Laptop", 799.99, "TechNova", 25, "15-inch laptop", "Electronics", "Laptops", "2 years"));
        addProductIfMissing(new PersonalCareProduct(104, "PC-001", "Face Wash", 12.99, "GlowCare", 60, "Gentle cleanser", "Personal Care", "Skincare", "Oily"));
        addProductIfMissing(new ElectronicsProduct(105, "EL-002", "Smartphone", 499.99, "MobilePlus", 40, "5G smartphone", "Electronics", "Mobile", "1 year"));
        addProductIfMissing(new PersonalCareProduct(106, "PC-002", "Shampoo", 8.49, "HairGlow", 90, "Moisturizing shampoo", "Personal Care", "Haircare", "Normal"));
        addProductIfMissing(new GroceryProduct(107, "GR-003", "Milk", 3.10, "DairyBest", 70, "Fresh milk", "Groceries", "Dairy", "2026-07-18"));

        addProductIfMissing(new ElectronicsProduct(108, "EL-003", "Headphones", 89.99, "SoundMax", 35, "Wireless headphones", "Electronics", "Audio", "1 year"));
        addProductIfMissing(new PersonalCareProduct(109, "PC-003", "Body Lotion", 14.50, "SoftGlow", 45, "Moisturizing body lotion", "Personal Care", "Bodycare", "Dry"));
        addProductIfMissing(new GroceryProduct(110, "GR-004", "Orange", 3.20, "CitrusFarm", 90, "Sweet oranges", "Groceries", "Fruits", "2026-08-05"));
        addProductIfMissing(new ElectronicsProduct(111, "EL-004", "Smart Watch", 149.99, "PulseTech", 28, "Fitness smartwatch", "Electronics", "Wearables", "2 years"));
        addProductIfMissing(new PersonalCareProduct(112, "PC-004", "Conditioner", 7.99, "HairGlow", 55, "Silky conditioner", "Personal Care", "Haircare", "Normal"));
        addProductIfMissing(new GroceryProduct(113, "GR-005", "Cake", 6.50, "BakeHouse", 30, "Chocolate cake", "Grocery", "Bakery", "2026-07-22"));
        addProductIfMissing(new ElectronicsProduct(114, "EL-005", "Tablet", 329.99, "TabWorld", 20, "Portable tablet", "Electronics", "Tablets", "1 year"));

        addProductIfMissing(new GroceryProduct(115, "GR-006", "Rice", 4.99, "GrainPlus", 60, "Long grain rice", "Groceries", "Staples", "2026-09-01"));
        addProductIfMissing(new PersonalCareProduct(116, "PC-005", "Toothpaste", 2.99, "FreshSmile", 70, "Fluoride toothpaste", "Personal Care", "Oralcare", "All"));
        addProductIfMissing(new GroceryProduct(117, "GR-007", "Cheese", 5.40, "DairyBest", 40, "Creamy cheese", "Grocery", "Dairy", "2026-07-25"));
        addProductIfMissing(new ElectronicsProduct(118, "EL-006", "Keyboard", 49.99, "KeyPro", 22, "Mechanical keyboard", "Electronics", "Accessories", "1 year"));
    }

    private void addCategoryIfMissing(Category category) {
        boolean exists = categories.stream().anyMatch(existing -> existing.getName().equalsIgnoreCase(category.getName()));
        if (!exists) {
            categories.add(category);
        }
    }

    private void addProductIfMissing(Product product) {
        boolean exists = products.stream().anyMatch(existing -> existing.getName().equalsIgnoreCase(product.getName())
                || existing.getSku().equalsIgnoreCase(product.getSku()));
        if (!exists) {
            products.add(product);
        }
    }

    public SearchResult search(String query) {
        String normalized = query.trim().toLowerCase();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }

        if (searchCache.contains(normalized)) {
            SearchResult cached = searchCache.get(normalized);
            return new SearchResult(cached.getType(), cached.getContent(), cached.getHierarchy(), true);
        }

        SearchResult result = findResult(normalized);
        if (result == null) {
            throw new IllegalArgumentException("No matching category, subcategory, or product found for: " + query);
        }

        searchCache.put(normalized, result);
        return new SearchResult(result.getType(), result.getContent(), result.getHierarchy(), false);
    }

    private SearchResult findResult(String query) {
        String lower = query.toLowerCase();

        for (Category category : categories) {
            if (category.getName().toLowerCase().contains(lower)) {
                StringBuilder builder = new StringBuilder();
                builder.append("Category: ").append(category.getName()).append("\n");
                builder.append("Description: ").append(category.getDescription()).append("\n");
                builder.append("Subcategories:\n");
                for (SubCategory sub : category.getSubCategories()) {
                    builder.append("- ").append(sub.getName()).append(" (")
                            .append(sub.getDescription()).append(")\n");
                }
                return new SearchResult("CATEGORY", builder.toString(), buildHierarchy(category.getName()), false);
            }
        }

        for (Category category : categories) {
            for (SubCategory subCategory : category.getSubCategories()) {
                if (subCategory.getName().toLowerCase().contains(lower)) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Subcategory: ").append(subCategory.getName()).append("\n");
                    builder.append("Description: ").append(subCategory.getDescription()).append("\n");
                    builder.append("Parent Category: ").append(category.getName()).append("\n");
                    builder.append("Products:\n");
                    boolean found = false;
                    for (Product product : products) {
                        if (product.getSubCategory().equalsIgnoreCase(subCategory.getName())) {
                            builder.append("- ").append(product.getName()).append(" | SKU: ")
                                    .append(product.getSku()).append(" | Price: ")
                                    .append(product.getPrice()).append("\n");
                            found = true;
                        }
                    }
                    if (!found) {
                        builder.append("- No products available under this subcategory.\n");
                    }
                    return new SearchResult("SUBCATEGORY", builder.toString(), buildHierarchy(category.getName(), subCategory.getName()), false);
                }
            }
        }

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(lower) || product.getSku().toLowerCase().contains(lower)) {
                StringBuilder builder = new StringBuilder();
                builder.append("Product: ").append(product.getName()).append("\n");
                builder.append("SKU: ").append(product.getSku()).append("\n");
                builder.append("Brand: ").append(product.getBrand()).append("\n");
                builder.append("Price: ").append(product.getPrice()).append("\n");
                builder.append("Stock: ").append(product.getStockQuantity()).append("\n");
                builder.append("Description: ").append(product.getDescription()).append("\n");
                builder.append("Category: ").append(product.getCategory()).append("\n");
                builder.append("Subcategory: ").append(product.getSubCategory()).append("\n");
                builder.append("Returnable: ").append(product.isReturnable() ? "Yes" : "No").append("\n");
                if (product instanceof GroceryProduct) {
                    builder.append("Expiry Date: ").append(((GroceryProduct) product).getExpiryDate()).append("\n");
                } else if (product instanceof ElectronicsProduct) {
                    builder.append("Warranty: ").append(((ElectronicsProduct) product).getWarrantyPeriod()).append("\n");
                } else if (product instanceof PersonalCareProduct) {
                    builder.append("Skin Type: ").append(((PersonalCareProduct) product).getSkinType()).append("\n");
                }
                return new SearchResult("PRODUCT", builder.toString(), buildHierarchy(product.getCategory(), product.getSubCategory(), product.getName()), false);
            }
        }

        return null;
    }

    private String buildHierarchy(String... names) {
        StringBuilder builder = new StringBuilder();
        builder.append("Hierarchy:\n");
        for (int i = 0; i < names.length; i++) {
            if (i == 0) {
                builder.append(names[i]);
            } else {
                builder.append(" > ").append(names[i]);
            }
        }
        return builder.toString();
    }

    public String displayHierarchy() {
        StringBuilder builder = new StringBuilder();
        builder.append("Category Hierarchy\n");
        builder.append("=================\n");
        for (Category category : categories) {
            builder.append("├── ").append(category.getName()).append("\n");
            for (SubCategory subCategory : category.getSubCategories()) {
                builder.append("│   └── ").append(subCategory.getName()).append("\n");
                boolean hasProducts = false;
                for (Product product : products) {
                    if (product.getCategory().equalsIgnoreCase(category.getName())
                            && product.getSubCategory().equalsIgnoreCase(subCategory.getName())) {
                        builder.append("│       └── ").append(product.getName())
                                .append(" (SKU: ").append(product.getSku()).append(")\n");
                        hasProducts = true;
                    }
                }
                if (!hasProducts) {
                    builder.append("│       └── No products in this subcategory\n");
                }
            }
        }
        return builder.toString();
    }

    public  String displayCache() {
        return searchCache.display();
    }
}
