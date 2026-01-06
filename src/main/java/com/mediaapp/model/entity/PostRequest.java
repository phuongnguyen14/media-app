package com.mediaapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * PostRequest Entity
 * Represents requests for content creation
 */
@Entity
@Table(name = "post_requests", indexes = {
    @Index(name = "idx_post_requests_requester", columnList = "requester_id"),
    @Index(name = "idx_post_requests_assignee", columnList = "assigned_to_id"),
    @Index(name = "idx_post_requests_category", columnList = "category_id"),
    @Index(name = "idx_post_requests_status", columnList = "status"),
    @Index(name = "idx_post_requests_priority", columnList = "priority"),
    @Index(name = "idx_post_requests_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "OPEN"; // OPEN, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_post_id")
    private Post relatedPost;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "need_sync", nullable = false)
    @Builder.Default
    private Boolean needSync = true;

    @Column(name = "completed_at")
    private Instant completedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // Helper methods
    public boolean isOpen() {
        return "OPEN".equals(status);
    }

    public boolean isAssigned() {
        return "ASSIGNED".equals(status);
    }

    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isOverdue() {
        return dueDate != null && 
               Instant.now().isAfter(dueDate) && 
               !isCompleted() && 
               !isCancelled();
    }

    public void assignTo(User user) {
        this.assignedTo = user;
        this.status = "ASSIGNED";
        this.needSync = true;
    }

    public void startProgress() {
        this.status = "IN_PROGRESS";
        this.needSync = true;
    }

    public void complete(Post post) {
        this.status = "COMPLETED";
        this.completedAt = Instant.now();
        this.relatedPost = post;
        this.needSync = true;
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.needSync = true;
    }

    public void markForSync() {
        this.needSync = true;
    }

    public void markSynced() {
        this.needSync = false;
    }
}
