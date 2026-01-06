package com.mediaapp.dto.response;

import lombok.*;

import java.time.Instant;

/**
 * PostRequest Response DTO
 * Full post request details for API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;

    private CategoryDto category;
    private UserDto requester;
    private UserDto assignedTo;
    private PostSummaryDto relatedPost;

    private Instant dueDate;
    private Instant completedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
