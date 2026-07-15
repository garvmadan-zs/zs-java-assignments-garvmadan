package zs.assignment3.model;

public abstract class Product {
    private final int id;
    private final String sku;
    private String name;
    private double price;
    private String brand;
    private int stockQuantity;
    private String description;
    private final String category;
    private String subCategory;
    private final boolean returnable;

    protected Product(int id, String sku, String name, double price, String brand,
                      int stockQuantity, String description, String category,
                      String subCategory, boolean returnable) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.returnable = returnable;
    }

    public int getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public abstract String getProductType();

    public String getDisplayDetails() {
        return String.format("[%s] %s | SKU: %s | Brand: %s | Price: %.2f | Stock: %d | Category: %s | Subcategory: %s | Returnable: %s",
                getProductType(), name, sku, brand, price, stockQuantity, category, subCategory, returnable ? "Yes" : "No");
    }
}
