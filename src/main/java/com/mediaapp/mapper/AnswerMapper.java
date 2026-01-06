package com.mediaapp.mapper;

import com.mediaapp.dto.request.CreateAnswerRequest;
import com.mediaapp.dto.response.AnswerResponse;
import com.mediaapp.model.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Answer Mapper
 * MapStruct mapper for Answer entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AnswerMapper {

    /**
     * Map Answer entity to AnswerResponse DTO
     */
    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "user", source = "user")
    AnswerResponse toResponse(Answer answer);

    /**
     * Map list of Answers to list of AnswerResponse
     */
    List<AnswerResponse> toResponseList(List<Answer> answers);

    /**
     * Map CreateAnswerRequest to Answer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true) // Will be set in service
    @Mapping(target = "user", ignore = true) // Will be set in service
    @Mapping(target = "isAccepted", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Answer toEntity(CreateAnswerRequest request);
}
