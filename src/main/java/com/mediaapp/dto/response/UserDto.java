package com.mediaapp.dto.response;

import lombok.*;

/**
 * User DTO
 * Lightweight user info for nested responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String fullName;
    private String avatarUrl;
    private String role;
}
