package com.mediaapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * MediaFile Entity
 * Represents uploaded media files
 */
@Entity
@Table(name = "media_files", indexes = {
    @Index(name = "idx_media_files_user", columnList = "user_id"),
    @Index(name = "idx_media_files_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_media_files_type", columnList = "file_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_type", nullable = false, length = 100)
    private String fileType; // IMAGE, VIDEO, DOCUMENT

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize; // in bytes

    @Column(name = "width")
    private Integer width; // for images/videos

    @Column(name = "height")
    private Integer height; // for images/videos

    @Column(name = "duration")
    private Integer duration; // for videos (in seconds)

    @Column(name = "entity_type", length = 50)
    private String entityType; // POST, QUESTION, COMMENT, etc.

    @Column(name = "entity_id")
    private Long entityId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Helper methods
    public boolean isImage() {
        return "IMAGE".equals(fileType);
    }

    public boolean isVideo() {
        return "VIDEO".equals(fileType);
    }

    public boolean isDocument() {
        return "DOCUMENT".equals(fileType);
    }

    public String getFileSizeMB() {
        return String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
    }

    public String getUrl() {
        // This would typically return the full URL to access the file
        return "/uploads/" + filePath;
    }
}
