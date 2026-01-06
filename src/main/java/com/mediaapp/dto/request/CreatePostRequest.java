package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

/**
 * Create Post Request DTO
 * Validation for creating new posts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @Size(max = 1000, message = "Summary must not exceed 1000 characters")
    private String summary;

    @NotBlank(message = "Content is required")
    @Size(min = 50, message = "Content must be at least 50 characters")
    private String content;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private Long topicId;

    private String featuredImageUrl;

    private Set<Long> tagIds;

    private String status; // Default will be DRAFT if not provided
}
