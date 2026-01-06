package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Category Repository
 * Data access layer for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by slug
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Find all root categories (categories without parent)
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true ORDER BY c.displayOrder")
    List<Category> findRootCategories();

    /**
     * Find all child categories of a parent
     */
    List<Category> findByParentIdAndIsActive(Long parentId, Boolean isActive);

    /**
     * Find all active categories
     */
    List<Category> findByIsActiveTrueOrderByDisplayOrder();

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);
}
