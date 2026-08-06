package com.zs.assignment1112.controller;

import com.zs.assignment1112.dto.response.CategoryResponse;
import com.zs.assignment1112.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller responsible for category related APIs.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {


    private static final Logger log =
            LogManager.getLogger(CategoryController.class);


    private final CategoryService categoryService;

    /**
     * Fetches all categories.
     *
     * @return list of categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        log.info("Request received to fetch all categories");

        List<CategoryResponse> categories =
                categoryService.getAllCategories();


        log.debug(
                "Returning {} categories",
                categories.size()
        );

        return ResponseEntity.ok(categories);
    }
}
