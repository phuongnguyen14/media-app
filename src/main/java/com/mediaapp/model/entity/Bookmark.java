package com.mediaapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Bookmark Entity
 * Represents bookmarks/saved items
 */
@Entity
@Table(name = "bookmarks", 
    indexes = {
        @Index(name = "idx_bookmarks_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_bookmarks_user", columnList = "user_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "bookmarks_unique", 
                         columnNames = {"user_id", "entity_type", "entity_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // POST, QUESTION

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Helper methods
    public boolean isPostBookmark() {
        return "POST".equals(entityType);
    }

    public boolean isQuestionBookmark() {
        return "QUESTION".equals(entityType);
    }

    public boolean hasNotes() {
        return notes != null && !notes.isEmpty();
    }
}
