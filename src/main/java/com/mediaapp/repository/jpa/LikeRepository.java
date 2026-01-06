package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Like Repository
 * Data access layer for Like entity
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Find like by user and entity
     */
    Optional<Like> findByUserIdAndEntityTypeAndEntityId(
        Long userId, 
        String entityType, 
        Long entityId
    );

    /**
     * Check if user liked an entity
     */
    boolean existsByUserIdAndEntityTypeAndEntityId(
        Long userId, 
        String entityType, 
        Long entityId
    );

    /**
     * Count likes for an entity
     */
    long countByEntityTypeAndEntityId(String entityType, Long entityId);

    /**
     * Delete like (unlike)
     */
    void deleteByUserIdAndEntityTypeAndEntityId(
        Long userId, 
        String entityType, 
        Long entityId
    );
}
