package com.mediaapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

/**
 * Update PostRequest Request DTO
 * Validation for updating existing post requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequestDto {

    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    private Long categoryId;

    @Pattern(regexp = "LOW|MEDIUM|HIGH|URGENT", message = "Priority must be LOW, MEDIUM, HIGH, or URGENT")
    private String priority;

    @Pattern(regexp = "OPEN|ASSIGNED|IN_PROGRESS|COMPLETED|CANCELLED", 
             message = "Status must be OPEN, ASSIGNED, IN_PROGRESS, COMPLETED, or CANCELLED")
    private String status;

    private Long assignedToId;

    private Instant dueDate;
}
