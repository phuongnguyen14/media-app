package com.mediaapp.dto.response;

import lombok.*;

import java.time.Instant;

/**
 * Answer Response DTO
 * Answer details for API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponse {

    private Long id;
    private Long questionId;
    private String content;
    private Boolean isAccepted;
    private Integer likeCount;

    private UserDto user;

    private Instant createdAt;
    private Instant updatedAt;
}
