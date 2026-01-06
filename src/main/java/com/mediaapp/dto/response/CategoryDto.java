package com.mediaapp.dto.response;

import lombok.*;

/**
 * Category DTO
 * Lightweight category info for nested responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
}
