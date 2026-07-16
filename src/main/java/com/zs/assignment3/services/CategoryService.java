package zs.assignment3.services;

import zs.assignment3.model.*;
import zs.assignment3.controller.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final List<Category> categories = new ArrayList<>();

    public CategoryService() {
        Category grocery = new Category(1, "Grocery", "Daily food and household essentials");
        grocery.addSubCategory(new SubCategory(1, "Fruits", "Fresh fruits"));
        grocery.addSubCategory(new SubCategory(2, "Vegetables", "Fresh vegetables"));
        categories.add(grocery);

        Category electronics = new Category(2, "Electronics", "Smart devices and accessories");
        electronics.addSubCategory(new SubCategory(3, "Mobile", "Smartphones and accessories"));
        electronics.addSubCategory(new SubCategory(4, "Laptops", "Portable computing"));
        categories.add(electronics);

        Category personalCare = new Category(3, "Personal Care", "Beauty and hygiene products");
        personalCare.addSubCategory(new SubCategory(5, "Skincare", "Cosmetics and skin products"));
        personalCare.addSubCategory(new SubCategory(6, "Haircare", "Shampoos and conditioners"));
        categories.add(personalCare);
    }

    public List<zs.assignment3.model.Category> getAllCategories() {
        return categories;
    }

    public Category addCategory(String name, String description) {
        Category category = new Category(categories.size() + 1, name, description);
        categories.add(category);
        return category;
    }

    public zs.assignment3.model.SubCategory addSubCategory(String categoryName, String subCategoryName, String description) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                SubCategory subCategory = new SubCategory(category.getSubCategories().size() + 1, subCategoryName, description);
                category.addSubCategory(subCategory);
                return subCategory;
            }
        }
        return null;
    }

    public zs.assignment3.model.Category findCategory(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
