package model;

public class GroceryProduct extends Product {
    private final String expiryDate;

    public GroceryProduct(int id, String sku, String name, double price, String brand,
                          int stockQuantity, String description, String category,
                          String subCategory, String expiryDate) {
        super(id, sku, name, price, brand, stockQuantity, description, category, subCategory, true);
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String getProductType() {
        return "Grocery";
    }
}
