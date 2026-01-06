package com.mediaapp.model.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Question Elasticsearch Document
 * Represents questions in Elasticsearch for full-text search
 */
@Document(indexName = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDocument {

    @Id
    private String id; // Same as database ID

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword)
    private String slug;

    @Field(type = FieldType.Long)
    private Long viewCount;

    @Field(type = FieldType.Integer)
    private Integer answerCount;

    @Field(type = FieldType.Boolean)
    private Boolean isPinned;

    @Field(type = FieldType.Boolean)
    private Boolean isFeatured;

    @Field(type = FieldType.Date)
    private Instant publishedAt;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Date)
    private Instant updatedAt;

    // Nested objects
    @Field(type = FieldType.Object)
    private CategoryEs category;

    @Field(type = FieldType.Object)
    private TopicEs topic;

    @Field(type = FieldType.Object)
    private UserEs user;

    @Field(type = FieldType.Nested)
    @Builder.Default
    private List<AnswerEs> answers = new ArrayList<>();

    @Field(type = FieldType.Keyword)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

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
    public static class TopicEs {
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
    public static class AnswerEs {
        private Long id;
        private String content;
        private Boolean isAccepted;
        private Integer likeCount;
        private UserEs user;
        private Instant createdAt;
    }
}
