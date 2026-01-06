package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

/**
 * Create Question Request DTO
 * Validation for creating new questions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 20, message = "Content must be at least 20 characters")
    private String content;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private Long topicId;

    private Set<Long> tagIds;

    private String status; // Default will be DRAFT if not provided
}
