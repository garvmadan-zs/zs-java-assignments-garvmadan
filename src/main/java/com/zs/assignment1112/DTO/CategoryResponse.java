package com.zs.assignment1112.DTO;

public class CategoryResponse {
    private int id;
    private String name;

    public CategoryResponse() {
    }

    public CategoryResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
