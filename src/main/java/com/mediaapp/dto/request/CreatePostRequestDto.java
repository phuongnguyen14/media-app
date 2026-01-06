package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

/**
 * Create PostRequest Request DTO
 * Validation for creating new post requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    private Long categoryId;

    @Pattern(regexp = "LOW|MEDIUM|HIGH|URGENT", message = "Priority must be LOW, MEDIUM, HIGH, or URGENT")
    private String priority; // Default will be MEDIUM if not provided

    private Instant dueDate;
}
