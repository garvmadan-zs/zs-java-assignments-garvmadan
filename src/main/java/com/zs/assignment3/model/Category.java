package model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final int id;
    private String name;
    private String description;
    private final List<SubCategory> subCategories = new ArrayList<>();

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void addSubCategory(SubCategory subCategory) {
        this.subCategories.add(subCategory);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
