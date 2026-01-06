package com.mediaapp.model.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

/**
 * PostRequest Elasticsearch Document
 * Represents post requests in Elasticsearch for full-text search
 */
@Document(indexName = "post_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDocument {

    @Id
    private String id; // Same as database ID

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Keyword)
    private String status; // OPEN, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED

    @Field(type = FieldType.Keyword)
    private String priority; // LOW, MEDIUM, HIGH, URGENT

    @Field(type = FieldType.Date)
    private Instant dueDate;

    @Field(type = FieldType.Date)
    private Instant completedAt;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Date)
    private Instant updatedAt;

    // Nested objects
    @Field(type = FieldType.Object)
    private CategoryEs category;

    @Field(type = FieldType.Object)
    private UserEs requester;

    @Field(type = FieldType.Object)
    private UserEs assignedTo;

    @Field(type = FieldType.Object)
    private PostEs relatedPost;

    // Nested classes for embedded objects
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryEs {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserEs {
        private Long id;
        private String username;
        private String fullName;
        private String avatarUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostEs {
        private Long id;
        private String title;
        private String slug;
    }
}
