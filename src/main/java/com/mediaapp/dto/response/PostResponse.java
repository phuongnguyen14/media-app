package com.mediaapp.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * Post Response DTO
 * Full post details for API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String status;

    private String featuredImageUrl;

    private Long viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isFeatured;
    private Boolean isPinned;

    private CategoryDto category;
    private TopicDto topic;
    private UserDto author;

    private List<TagDto> tags;

    private Instant publishedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
