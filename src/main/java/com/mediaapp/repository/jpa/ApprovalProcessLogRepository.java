package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.ApprovalProcessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ApprovalProcessLog Repository
 * Data access layer for ApprovalProcessLog entity
 */
@Repository
public interface ApprovalProcessLogRepository extends JpaRepository<ApprovalProcessLog, Long> {

    /**
     * Find approval logs by entity type and ID
     */
    @EntityGraph(attributePaths = {"approver"})
    List<ApprovalProcessLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
        String entityType, 
        Long entityId
    );

    /**
     * Find approval logs by approver
     */
    @EntityGraph(attributePaths = {})
    Page<ApprovalProcessLog> findByApproverId(Long approverId, Pageable pageable);

    /**
     * Find approval logs by action
     */
    List<ApprovalProcessLog> findByAction(String action);
}
