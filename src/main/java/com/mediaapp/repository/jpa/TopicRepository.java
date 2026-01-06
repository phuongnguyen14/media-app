package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Topic Repository
 * Data access layer for Topic entity
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    /**
     * Find topic by slug within a category
     */
    Optional<Topic> findByCategoryIdAndSlug(Long categoryId, String slug);

    /**
     * Find all topics for a category
     */
    List<Topic> findByCategoryIdAndIsActiveTrueOrderByDisplayOrder(Long categoryId);

    /**
     * Find all active topics
     */
    List<Topic> findByIsActiveTrueOrderByCategoryIdAscDisplayOrderAsc();

    /**
     * Check if slug exists within category
     */
    boolean existsByCategoryIdAndSlug(Long categoryId, String slug);

    /**
     * Count topics in a category
     */
    long countByCategoryIdAndIsActive(Long categoryId, Boolean isActive);

    /**
     * Find topic by slug (simple version without category filter)
     */
    Optional<Topic> findBySlug(String slug);

    /**
     * Find all topics by category ID
     */
    List<Topic> findByCategoryId(Long categoryId);
}
