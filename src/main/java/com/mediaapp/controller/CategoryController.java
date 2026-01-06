package com.mediaapp.controller;

import com.mediaapp.model.entity.Category;
import com.mediaapp.repository.jpa.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category REST Controller
 * Handles category management operations
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "API for managing content categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Operation(summary = "Get all categories", description = "Retrieve a paginated list of all categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(categoryRepository.findAll(pageable));
    }

    @Operation(summary = "Get all categories (no pagination)", description = "Retrieve all categories without pagination")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategoriesNoPagination() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID of the category") @PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get category by slug", description = "Retrieve a category by its URL-friendly slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(
            @Parameter(description = "Slug of the category") @PathVariable String slug) {
        return categoryRepository.findBySlug(slug)
                .map(category -> ResponseEntity.ok(category))
                .orElse(ResponseEntity.notFound().build());
    }
}
