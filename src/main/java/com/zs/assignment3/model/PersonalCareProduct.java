package model;

public class PersonalCareProduct extends Product {
    private final String skinType;

    public PersonalCareProduct(int id, String sku, String name, double price, String brand,
                               int stockQuantity, String description, String category,
                               String subCategory, String skinType) {
        super(id, sku, name, price, brand, stockQuantity, description, category, subCategory, false);
        this.skinType = skinType;
    }

    public String getSkinType() {
        return skinType;
    }

    @Override
    public String getProductType() {
        return "Personal Care";
    }
}
