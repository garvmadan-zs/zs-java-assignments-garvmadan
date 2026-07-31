package com.zs.assignment1112.DTO;

public class ProductResponse {
    private int id;
    private String name;
    private Double price;
    private CategoryResponse categoryResponse;

    public ProductResponse() {
    }

    public ProductResponse(int id, String name, Double price, CategoryResponse categoryResponse) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryResponse = categoryResponse;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public CategoryResponse getCategoryResponse() {
        return categoryResponse;
    }
}
