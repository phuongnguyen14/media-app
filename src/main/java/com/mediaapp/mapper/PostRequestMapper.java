package com.mediaapp.mapper;

import com.mediaapp.dto.request.CreatePostRequestDto;
import com.mediaapp.dto.request.UpdatePostRequestDto;
import com.mediaapp.dto.response.PostRequestResponse;
import com.mediaapp.model.entity.PostRequest;
import org.mapstruct.*;

import java.util.List;

/**
 * PostRequest Mapper
 * MapStruct mapper for PostRequest entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, PostMapper.class})
public interface PostRequestMapper {

    /**
     * Map PostRequest entity to PostRequestResponse DTO
     */
    @Mapping(target = "requester", source = "requester")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "assignedTo", source = "assignedTo")
    @Mapping(target = "relatedPost", source = "relatedPost")
    PostRequestResponse toResponse(PostRequest postRequest);

    /**
     * Map list of PostRequests to list of PostRequestResponse
     */
    List<PostRequestResponse> toResponseList(List<PostRequest> postRequests);

    /**
     * Map CreatePostRequestDto to PostRequest entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requester", ignore = true) // Will be set in service
    @Mapping(target = "category", ignore = true) // Will be set in service
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "relatedPost", ignore = true)
    @Mapping(target = "needSync", constant = "true")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    PostRequest toEntity(CreatePostRequestDto request);

    /**
     * Update PostRequest entity from UpdatePostRequestDto
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "category", ignore = true) // Will be set in service
    @Mapping(target = "assignedTo", ignore = true) // Will be set in service
    @Mapping(target = "relatedPost", ignore = true)
    @Mapping(target = "needSync", constant = "true")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdatePostRequestDto request, @MappingTarget PostRequest postRequest);
}
