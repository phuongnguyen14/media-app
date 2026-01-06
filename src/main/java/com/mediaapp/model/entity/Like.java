package com.mediaapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Like Entity
 * Represents likes on various entities (polymorphic)
 */
@Entity
@Table(name = "likes", 
    indexes = {
        @Index(name = "idx_likes_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_likes_user", columnList = "user_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "likes_unique", 
                         columnNames = {"user_id", "entity_type", "entity_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // POST, ANSWER, COMMENT

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Helper methods
    public boolean isPostLike() {
        return "POST".equals(entityType);
    }

    public boolean isAnswerLike() {
        return "ANSWER".equals(entityType);
    }

    public boolean isCommentLike() {
        return "COMMENT".equals(entityType);
    }
}
