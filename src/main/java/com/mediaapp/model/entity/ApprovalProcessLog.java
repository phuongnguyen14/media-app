package com.mediaapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * ApprovalProcessLog Entity
 * Represents approval/rejection actions on content
 */
@Entity
@Table(name = "approval_process_logs", indexes = {
    @Index(name = "idx_approval_logs_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_approval_logs_approver", columnList = "approver_id"),
    @Index(name = "idx_approval_logs_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalProcessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // QUESTION, POST, etc.

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Column(name = "previous_status", length = 50)
    private String previousStatus;

    @Column(name = "new_status", nullable = false, length = 50)
    private String newStatus;

    @Column(nullable = false, length = 50)
    private String action; // APPROVE, REJECT, REQUEST_CHANGES

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Helper methods
    public boolean isApproval() {
        return "APPROVE".equals(action);
    }

    public boolean isRejection() {
        return "REJECT".equals(action);
    }

    public boolean isChangeRequest() {
        return "REQUEST_CHANGES".equals(action);
    }

    public boolean hasComment() {
        return comment != null && !comment.isEmpty();
    }
}
