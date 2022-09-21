package com.edu.ulab.app.dtomapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User userDtoToUserEntity(UserDto userDto);

    UserDto userEntityToUserDto(User user);
}
