package com.mediaapp.dto.response;

import lombok.*;

import java.time.Instant;

/**
 * Comment Response DTO
 * Comment details for API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long parentCommentId;
    private String content;
    private Integer likeCount;

    private UserDto user;

    private Instant createdAt;
    private Instant updatedAt;
}
