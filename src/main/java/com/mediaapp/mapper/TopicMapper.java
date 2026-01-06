package com.mediaapp.mapper;

import com.mediaapp.dto.response.TopicDto;
import com.mediaapp.model.entity.Topic;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Topic Mapper
 * MapStruct mapper for Topic entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface TopicMapper {

    /**
     * Map Topic entity to TopicDto
     */
    TopicDto toDto(Topic topic);

    /**
     * Map list of Topics to list of TopicDto
     */
    List<TopicDto> toDtoList(List<Topic> topics);
}
