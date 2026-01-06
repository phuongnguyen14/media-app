package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

/**
 * Update Question Request DTO
 * Validation for updating existing questions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionRequest {

    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @Size(min = 20, message = "Content must be at least 20 characters")
    private String content;

    private Long categoryId;

    private Long topicId;

    private Set<Long> tagIds;

    private String status;

    private Boolean isPinned;

    private Boolean isFeatured;
}
