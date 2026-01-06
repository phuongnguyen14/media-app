package com.mediaapp.mapper;

import com.mediaapp.dto.response.CategoryDto;
import com.mediaapp.model.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Category Mapper
 * MapStruct mapper for Category entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Map Category entity to CategoryDto
     */
    CategoryDto toDto(Category category);

    /**
     * Map list of Categories to list of CategoryDto
     */
    List<CategoryDto> toDtoList(List<Category> categories);
}
