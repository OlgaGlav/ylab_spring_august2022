package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(Long id);

    void deleteById(Long id);
}
