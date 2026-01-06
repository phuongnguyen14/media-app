package com.mediaapp.dto.response;

import lombok.*;

/**
 * Topic DTO
 * Lightweight topic info for nested responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
}
