package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Post Repository
 * Data access layer for Post entity
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find post by ID with eager loading
     */
    @EntityGraph(attributePaths = {"category", "topic", "author", "tags"})
    Optional<Post> findWithDetailsById(Long id);

    /**
     * Find post by slug
     */
    @EntityGraph(attributePaths = {"category", "topic", "author"})
    Optional<Post> findBySlug(String slug);

    /**
     * Find posts that need sync with eager loading to avoid N+1
     */
    @EntityGraph(attributePaths = {"category", "topic", "author", "tags"})
    @Query("SELECT p FROM Post p WHERE p.needSync = true AND p.deletedAt IS NULL")
    List<Post> findNeedSync(Pageable pageable);

    /**
     * Find posts by status
     */
    @EntityGraph(attributePaths = {"category", "topic", "author"})
    Page<Post> findByStatusAndDeletedAtIsNull(String status, Pageable pageable);

    /**
     * Find posts by category
     */
    @EntityGraph(attributePaths = {"topic", "author"})
    Page<Post> findByCategoryIdAndStatusAndDeletedAtIsNull(
        Long categoryId, 
        String status, 
        Pageable pageable
    );

    /**
     * Find posts by topic
     */
    @EntityGraph(attributePaths = {"category", "author"})
    Page<Post> findByTopicIdAndStatusAndDeletedAtIsNull(
        Long topicId, 
        String status, 
        Pageable pageable
    );

    /**
     * Find posts by author
     */
    @EntityGraph(attributePaths = {"category", "topic"})
    Page<Post> findByAuthorIdAndDeletedAtIsNull(Long authorId, Pageable pageable);

    /**
     * Find featured posts
     */
    @EntityGraph(attributePaths = {"category", "topic", "author"})
    @Query("SELECT p FROM Post p WHERE p.isFeatured = true AND p.status = 'PUBLISHED' AND p.deletedAt IS NULL")
    List<Post> findFeatured(Pageable pageable);

    /**
     * Search posts by title or content
     */
    @Query("SELECT p FROM Post p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND p.status = 'PUBLISHED' AND p.deletedAt IS NULL")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Count posts by status
     */
    long countByStatus(String status);

    /**
     * Count posts by author
     */
    long countByAuthorIdAndDeletedAtIsNull(Long authorId);
}
