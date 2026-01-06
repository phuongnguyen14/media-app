package com.mediaapp.mapper;

import com.mediaapp.dto.request.CreateCommentRequest;
import com.mediaapp.dto.response.CommentResponse;
import com.mediaapp.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Comment Mapper
 * MapStruct mapper for Comment entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    /**
     * Map Comment entity to CommentResponse DTO
     */
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "parentCommentId", source = "parentComment.id")
    @Mapping(target = "user", source = "user")
    CommentResponse toResponse(Comment comment);

    /**
     * Map list of Comments to list of CommentResponse
     */
    List<CommentResponse> toResponseList(List<Comment> comments);

    /**
     * Map CreateCommentRequest to Comment entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true) // Will be set in service
    @Mapping(target = "user", ignore = true) // Will be set in service
    @Mapping(target = "parentComment", ignore = true) // Will be set in service
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Comment toEntity(CreateCommentRequest request);
}
