package com.mediaapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * QuestionAssignment Entity
 * Represents assignments of questions to specific users
 */
@Entity
@Table(name = "question_assignments", 
    indexes = {
        @Index(name = "idx_question_assignments_question", columnList = "question_id"),
        @Index(name = "idx_question_assignments_assignee", columnList = "assigned_to_id"),
        @Index(name = "idx_question_assignments_status", columnList = "status")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "question_assignments_unique", 
                         columnNames = {"question_id", "assigned_to_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_by_id", nullable = false)
    private User assignedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED

    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    // Helper methods
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public void startWork() {
        this.status = "IN_PROGRESS";
    }

    public void complete() {
        this.status = "COMPLETED";
        this.completedAt = Instant.now();
    }
}
