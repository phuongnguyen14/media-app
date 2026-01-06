package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Question;
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
 * Question Repository
 * Data access layer for Question entity
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Find question by ID with eager loading
     */
    @EntityGraph(attributePaths = {"category", "topic", "user", "tags"})
    Optional<Question> findWithDetailsById(Long id);

    /**
     * Find question by slug
     */
    @EntityGraph(attributePaths = {"category", "topic", "user"})
    Optional<Question> findBySlug(String slug);

    /**
     * Find questions that need sync with eager loading to avoid N+1
     */
    @EntityGraph(attributePaths = {"category", "topic", "user", "tags"})
    @Query("SELECT q FROM Question q WHERE q.needSync = true AND q.deletedAt IS NULL")
    List<Question> findNeedSync(Pageable pageable);

    /**
     * Find questions by status
     */
    @EntityGraph(attributePaths = {"category", "topic", "user"})
    Page<Question> findByStatusAndDeletedAtIsNull(String status, Pageable pageable);

    /**
     * Find questions by category
     */
    @EntityGraph(attributePaths = {"topic", "user"})
    Page<Question> findByCategoryIdAndStatusAndDeletedAtIsNull(
        Long categoryId, 
        String status, 
        Pageable pageable
    );

    /**
     * Find questions by topic
     */
    @EntityGraph(attributePaths = {"category", "user"})
    Page<Question> findByTopicIdAndStatusAndDeletedAtIsNull(
        Long topicId, 
        String status, 
        Pageable pageable
    );

    /**
     * Find questions by user
     */
    @EntityGraph(attributePaths = {"category", "topic"})
    Page<Question> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    /**
     * Find featured questions
     */
    @EntityGraph(attributePaths = {"category", "topic", "user"})
    @Query("SELECT q FROM Question q WHERE q.isFeatured = true AND q.status = 'PUBLISHED' AND q.deletedAt IS NULL")
    List<Question> findFeatured(Pageable pageable);

    /**
     * Search questions by title or content
     */
    @Query("SELECT q FROM Question q WHERE " +
           "(LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND q.status = 'PUBLISHED' AND q.deletedAt IS NULL")
    Page<Question> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Count questions by status
     */
    long countByStatus(String status);

    /**
     * Count questions by user
     */
    long countByUserIdAndDeletedAtIsNull(Long userId);
}
