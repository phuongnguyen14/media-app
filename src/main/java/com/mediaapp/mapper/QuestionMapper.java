package com.mediaapp.mapper;

import com.mediaapp.dto.request.CreateQuestionRequest;
import com.mediaapp.dto.request.UpdateQuestionRequest;
import com.mediaapp.dto.response.QuestionResponse;
import com.mediaapp.model.entity.Question;
import org.mapstruct.*;

import java.util.List;

/**
 * Question Mapper
 * MapStruct mapper for Question entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, TopicMapper.class, UserMapper.class, TagMapper.class})
public interface QuestionMapper {

    /**
     * Map Question entity to QuestionResponse DTO
     */
    @Mapping(target = "user", source = "user")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "tags", source = "tags")
    QuestionResponse toResponse(Question question);

    /**
     * Map list of Questions to list of QuestionResponse
     */
    List<QuestionResponse> toResponseList(List<Question> questions);

    /**
     * Map CreateQuestionRequest to Question entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Will be set in service
    @Mapping(target = "topic", ignore = true) // Will be set in service
    @Mapping(target = "user", ignore = true) // Will be set in service
    @Mapping(target = "tags", ignore = true) // Will be set in service
    @Mapping(target = "slug", ignore = true) // Will be generated in service
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "answerCount", ignore = true)
    @Mapping(target = "isPinned", ignore = true)
    @Mapping(target = "isFeatured", ignore = true)
    @Mapping(target = "needSync", constant = "true")
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Question toEntity(CreateQuestionRequest request);

    /**
     * Update Question entity from UpdateQuestionRequest
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Will be set in service
    @Mapping(target = "topic", ignore = true) // Will be set in service
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tags", ignore = true) // Will be set in service
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "answerCount", ignore = true)
    @Mapping(target = "needSync", constant = "true")
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdateQuestionRequest request, @MappingTarget Question question);
}
