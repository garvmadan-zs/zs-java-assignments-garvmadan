package zs.assignment3.model;

public class ElectronicsProduct extends Product {
    private final String warrantyPeriod;

    public ElectronicsProduct(int id, String sku, String name, double price, String brand,
                              int stockQuantity, String description, String category,
                              String subCategory, String warrantyPeriod) {
        super(id, sku, name, price, brand, stockQuantity, description, category, subCategory, true);
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    @Override
    public String getProductType() {
        return "Electronics";
    }
}
