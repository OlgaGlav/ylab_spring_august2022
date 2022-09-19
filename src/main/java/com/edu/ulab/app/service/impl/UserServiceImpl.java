package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.dtomapper.UserEntityMapper;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static Long id = 0L;

//    @Qualifier("user_repository")
//    private final AbstractStorage repository;

    private final UserStorage repository;
    private final UserEntityMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapper.userDtoToUserEntity(userDto);
        user.setId(++id);
        repository.save(user);
        userDto.setId(id);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        repository.save(mapper.userDtoToUserEntity(userDto));
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = (User) repository.findById(id);
        return mapper.userEntityToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        repository.delete(id);
    }
}
