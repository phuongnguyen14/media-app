package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Comment Repository
 * Data access layer for Comment entity
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find comments by post ID (top-level only)
     */
    @EntityGraph(attributePaths = {"user"})
    Page<Comment> findByPostIdAndParentCommentIsNullAndDeletedAtIsNull(
        Long postId, 
        Pageable pageable
    );

    /**
     * Find comments by post IDs (for bulk fetching)
     */
    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByPostIdInAndDeletedAtIsNull(List<Long> postIds);

    /**
     * Find replies to a comment
     */
    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByParentCommentIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long parentCommentId);

    /**
     * Find comments by user
     */
    @EntityGraph(attributePaths = {"post"})
    Page<Comment> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    /**
     * Count comments by post
     */
    long countByPostIdAndDeletedAtIsNull(Long postId);

    /**
     * Count replies to a comment
     */
    long countByParentCommentIdAndDeletedAtIsNull(Long parentCommentId);

    /**
     * Count comments by user
     */
    long countByUserIdAndDeletedAtIsNull(Long userId);
}
