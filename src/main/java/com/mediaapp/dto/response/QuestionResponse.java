package com.mediaapp.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * Question Response DTO
 * Full question details for API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long id;
    private String title;
    private String content;
    private String status;
    private String slug;

    private Long viewCount;
    private Integer answerCount;
    private Boolean isPinned;
    private Boolean isFeatured;

    private CategoryDto category;
    private TopicDto topic;
    private UserDto user;

    private List<TagDto> tags;

    private Instant publishedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
