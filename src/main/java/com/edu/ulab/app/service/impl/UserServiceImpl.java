package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.dtomapper.UserEntityMapper;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.AbstractStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static Long id = 1L;

    private final AbstractStorage storage;
    private final UserEntityMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        //todo
        // сгенерировать идентификатор
        // создать пользователя
        User user = new User();
        user.setId(id++);
        user.setAge(userDto.getAge());
        user.setTitle(user.getTitle());
        user.setFullName(userDto.getFullName());
        // todo вернуть сохраненного пользователя со всеми необходимыми полями id
        userDto.setId(id++);
        storage.save(user);

        return mapper.userEntityToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        //todo
        return null;
    }

    @Override
    public UserDto getUserById(Long id) {
        //todo
        return null;
    }

    @Override
    public void deleteUserById(Long id) {
        //todo
    }
}
