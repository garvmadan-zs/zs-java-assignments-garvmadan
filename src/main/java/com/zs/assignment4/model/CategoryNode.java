package zs.assignment4.model;

import zs.assignment3.model.SubCategory;
import zs.assignment3.model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryNode {
    private final String name;
    private final Category category;
    private final SubCategory subCategory;
    private final List<CategoryNode> children = new ArrayList<>();
    private CategoryNode parent;

    public CategoryNode(String name) {
        this(name, null, null, null);
    }

    public CategoryNode(String name, Category category) {
        this(name, category, null, null);
    }

    public CategoryNode(String name, Category category, SubCategory subCategory, CategoryNode parent) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public CategoryNode getParent() {
        return parent;
    }

    public List<CategoryNode> getChildren() {
        return children;
    }

    public void addChild(CategoryNode child) {
        child.parent = this;
        children.add(child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public String getPath() {
        if (parent == null) {
            return name;
        }
        return parent.getPath() + " > " + name;
    }
}
