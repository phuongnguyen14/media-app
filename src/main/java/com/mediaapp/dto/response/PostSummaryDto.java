package com.mediaapp.dto.response;

import lombok.*;

/**
 * Post Summary DTO
 * Lightweight post info for nested responses (e.g., in PostRequest)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSummaryDto {

    private Long id;
    private String title;
    private String slug;
    private String status;
}
