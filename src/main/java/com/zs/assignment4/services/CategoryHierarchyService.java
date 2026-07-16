package zs.assignment4.services;


import zs.assignment4.model.CategoryNode;
import zs.assignment3.model.SubCategory;
import zs.assignment3.model.Category;
import zs.assignment3.services.CategoryService;
import java.util.ArrayList;
import java.util.List;

public class CategoryHierarchyService {
    private final CategoryNode root;
    private final CategoryService categoryService;

    public CategoryHierarchyService() {
        this.categoryService = new CategoryService();
        this.root = new CategoryNode("Ecommerce");
        buildHierarchy();
    }

    private void buildHierarchy() {
        for (Category category : categoryService.getAllCategories()) {
            CategoryNode categoryNode = new CategoryNode(category.getName(), category);
            root.addChild(categoryNode);

            for (SubCategory subCategory : category.getSubCategories()) {
                categoryNode.addChild(new CategoryNode(subCategory.getName(), category, subCategory, categoryNode));
            }
        }
    }

    public String displayHierarchy() {
        StringBuilder builder = new StringBuilder();
        printHierarchy(root, 0, builder);
        return builder.toString();
    }

    private void printHierarchy(CategoryNode node, int level, StringBuilder builder) {
        builder.append("  ".repeat(level)).append(node.getName()).append("\n");
        for (CategoryNode child : node.getChildren()) {
            printHierarchy(child, level + 1, builder);
        }
    }

    public List<CategoryNode> searchCategory(String keyword) {
        List<CategoryNode> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        searchCategory(root, keyword.toLowerCase(), results);
        return results;
    }

    private void searchCategory(CategoryNode node, String keyword, List<CategoryNode> results) {
        if (node.getName().toLowerCase().contains(keyword)) {
            results.add(node);
        }
        for (CategoryNode child : node.getChildren()) {
            searchCategory(child, keyword, results);
        }
    }

    public CategoryNode findCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return findCategory(root, name.toLowerCase());
    }

    private CategoryNode findCategory(CategoryNode node, String keyword) {
        if (node.getName().equalsIgnoreCase(keyword)) {
            return node;
        }
        for (CategoryNode child : node.getChildren()) {
            CategoryNode found = findCategory(child, keyword);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
