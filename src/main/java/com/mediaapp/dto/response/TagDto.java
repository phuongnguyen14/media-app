package com.mediaapp.dto.response;

import lombok.*;

/**
 * Tag DTO
 * Tag info for responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto {

    private Long id;
    private String name;
    private String slug;
    private Integer usageCount;
}
