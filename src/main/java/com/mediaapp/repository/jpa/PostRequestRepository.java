package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.PostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * PostRequest Repository
 * Data access layer for PostRequest entity
 */
@Repository
public interface PostRequestRepository extends JpaRepository<PostRequest, Long> {

    /**
     * Find post request by ID with eager loading
     */
    @EntityGraph(attributePaths = {"requester", "category", "assignedTo", "relatedPost"})
    Optional<PostRequest> findWithDetailsById(Long id);

    /**
     * Find post requests that need sync with eager loading
     */
    @EntityGraph(attributePaths = {"requester", "category", "assignedTo"})
    @Query("SELECT pr FROM PostRequest pr WHERE pr.needSync = true AND pr.deletedAt IS NULL")
    List<PostRequest> findNeedSync(Pageable pageable);

    /**
     * Find post requests by status
     */
    @EntityGraph(attributePaths = {"requester", "category", "assignedTo"})
    Page<PostRequest> findByStatusAndDeletedAtIsNull(String status, Pageable pageable);

    /**
     * Find post requests by requester
     */
    @EntityGraph(attributePaths = {"category", "assignedTo"})
    Page<PostRequest> findByRequesterIdAndDeletedAtIsNull(Long requesterId, Pageable pageable);

    /**
     * Find post requests assigned to a user
     */
    @EntityGraph(attributePaths = {"requester", "category"})
    Page<PostRequest> findByAssignedToIdAndDeletedAtIsNull(Long assignedToId, Pageable pageable);

    /**
     * Find post requests by category
     */
    @EntityGraph(attributePaths = {"requester", "assignedTo"})
    Page<PostRequest> findByCategoryIdAndStatusAndDeletedAtIsNull(
        Long categoryId, 
        String status, 
        Pageable pageable
    );

    /**
     * Find overdue post requests
     */
    @Query("SELECT pr FROM PostRequest pr WHERE " +
           "pr.dueDate < :now AND " +
           "pr.status NOT IN ('COMPLETED', 'CANCELLED') AND " +
           "pr.deletedAt IS NULL")
    List<PostRequest> findOverdue(Instant now);

    /**
     * Find open post requests
     */
    @EntityGraph(attributePaths = {"requester", "category"})
    @Query("SELECT pr FROM PostRequest pr WHERE pr.status = 'OPEN' AND pr.deletedAt IS NULL")
    Page<PostRequest> findOpen(Pageable pageable);

    /**
     * Count post requests by status
     */
    long countByStatus(String status);

    /**
     * Count post requests by requester
     */
    long countByRequesterIdAndDeletedAtIsNull(Long requesterId);

    /**
     * Count assigned post requests
     */
    long countByAssignedToIdAndStatusNotIn(Long assignedToId, List<String> excludedStatuses);
}
