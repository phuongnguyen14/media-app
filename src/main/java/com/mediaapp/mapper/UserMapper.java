package com.mediaapp.mapper;

import com.mediaapp.dto.response.UserDto;
import com.mediaapp.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * User Mapper
 * MapStruct mapper for User entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map User entity to UserDto
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserDto toDto(User user);

    /**
     * Map list of Users to list of UserDto
     */
    List<UserDto> toDtoList(List<User> users);
}
