package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

/**
 * Update Post Request DTO
 * Validation for updating existing posts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequest {

    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @Size(max = 1000, message = "Summary must not exceed 1000 characters")
    private String summary;

    @Size(min = 50, message = "Content must be at least 50 characters")
    private String content;

    private Long categoryId;

    private Long topicId;

    private String featuredImageUrl;

    private Set<Long> tagIds;

    private String status;

    private Boolean isPinned;

    private Boolean isFeatured;
}
