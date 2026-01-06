package com.mediaapp.mapper;

import com.mediaapp.dto.request.CreatePostRequest;
import com.mediaapp.dto.request.UpdatePostRequest;
import com.mediaapp.dto.response.PostResponse;
import com.mediaapp.model.entity.Post;
import org.mapstruct.*;

import java.util.List;

/**
 * Post Mapper
 * MapStruct mapper for Post entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, TopicMapper.class, UserMapper.class, TagMapper.class})
public interface PostMapper {

    /**
     * Map Post entity to PostResponse DTO
     */
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "tags", source = "tags")
    PostResponse toResponse(Post post);

    /**
     * Map list of Posts to list of PostResponse
     */
    List<PostResponse> toResponseList(List<Post> posts);

    /**
     * Map CreatePostRequest to Post entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Will be set in service
    @Mapping(target = "topic", ignore = true) // Will be set in service
    @Mapping(target = "author", ignore = true) // Will be set in service
    @Mapping(target = "tags", ignore = true) // Will be set in service
    @Mapping(target = "slug", ignore = true) // Will be generated in service
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "isPinned", ignore = true)
    @Mapping(target = "isFeatured", ignore = true)
    @Mapping(target = "needSync", constant = "true")
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Post toEntity(CreatePostRequest request);

    /**
     * Update Post entity from UpdatePostRequest
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Will be set in service
    @Mapping(target = "topic", ignore = true) // Will be set in service
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true) // Will be set in service
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "needSync", constant = "true")
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdatePostRequest request, @MappingTarget Post post);
}
