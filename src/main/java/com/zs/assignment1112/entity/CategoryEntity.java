package com.zs.assignment1112.entity;


import com.zs.assignment1112.DTO.ProductResponse;
import jakarta.persistence.*;


@Entity
@Table(name="categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany()





}
