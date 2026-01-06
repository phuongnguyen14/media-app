package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Create Answer Request DTO
 * Validation for creating answers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAnswerRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Content is required")
    @Size(min = 20, message = "Content must be at least 20 characters")
    private String content;
}
