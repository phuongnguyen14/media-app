package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Create Comment Request DTO
 * Validation for creating comments
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {

    @NotNull(message = "Post ID is required")
    private Long postId;

    private Long parentCommentId; // For nested comments/replies

    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 2000, message = "Content must be between 5 and 2000 characters")
    private String content;
}
