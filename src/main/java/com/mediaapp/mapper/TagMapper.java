package com.mediaapp.mapper;

import com.mediaapp.dto.response.TagDto;
import com.mediaapp.model.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Tag Mapper
 * MapStruct mapper for Tag entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface TagMapper {

    /**
     * Map Tag entity to TagDto
     */
    TagDto toDto(Tag tag);

    /**
     * Map list of Tags to list of TagDto
     */
    List<TagDto> toDtoList(List<Tag> tags);

    /**
     * Map set of Tags to list of TagDto
     */
    List<TagDto> toDtoList(Set<Tag> tags);
}
