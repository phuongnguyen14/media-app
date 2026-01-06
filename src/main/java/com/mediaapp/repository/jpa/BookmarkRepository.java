package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Bookmark Repository
 * Data access layer for Bookmark entity
 */
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    /**
     * Find bookmark by user and entity
 */
    Optional<Bookmark> findByUserIdAndEntityTypeAndEntityId(
        Long userId, 
        String entityType, 
        Long entityId
    );

    /**
     * Check if user bookmarked an entity
     */
    boolean existsByUserIdAndEntityTypeAndEntityId(
        Long userId, 
        String entityType, 
        Long entityId
    );

    /**
     * Find bookmarks by user
     */
    Page<Bookmark> findByUserId(Long userId, Pageable pageable);

    /**
     * Find bookmarks by user and entity type
     */
    Page<Bookmark> findByUserIdAndEntityType(
        Long userId, 
        String entityType, 
        Pageable pageable
    );

    /**
     * Delete bookmark (unbookmark)
     */
    void deleteByUserIdAndEntityTypeAndEntityId(
        Long userId, 
        String entityType, 
        Long entityId
    );

    /**
     * Count bookmarks by user
     */
    long countByUserId(Long userId);
}
