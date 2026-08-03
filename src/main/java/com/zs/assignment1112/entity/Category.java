package com.zs.assignment1112.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a product category.
 * <p>
 * A category can contain multiple products.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    /**
     * Unique identifier of the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Category name.
     */
    @Column(nullable = false, unique = true)
    private String name;
    /**
     * Products belonging to this category.
     */
    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {

        products.add(product);

        product.setCategory(this);
    }

    public void removeProduct(Product product) {

        products.remove(product);

        product.setCategory(null);
    }
}
