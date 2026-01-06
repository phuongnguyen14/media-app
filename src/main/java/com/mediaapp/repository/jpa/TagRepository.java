package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Tag Repository
 * Data access layer for Tag entity
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find tag by name
     */
    Optional<Tag> findByName(String name);

    /**
     * Find tag by slug
     */
    Optional<Tag> findBySlug(String slug);

    /**
     * Find tags by IDs
     */
    List<Tag> findByIdIn(Set<Long> ids);

    /**
     * Find popular tags
     */
    @Query("SELECT t FROM Tag t ORDER BY t.usageCount DESC")
    List<Tag> findPopularTags(org.springframework.data.domain.Pageable pageable);

    /**
     * Search tags by name
     */
    List<Tag> findByNameContainingIgnoreCase(String name);

    /**
     * Check if name exists
     */
    boolean existsByName(String name);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);
}
