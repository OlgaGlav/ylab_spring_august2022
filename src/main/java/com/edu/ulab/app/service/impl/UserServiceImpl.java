package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        Person person = mapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", person);
        Person savedUser = repository.save(person);
        log.info("Saved user: {}", savedUser);
        return mapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        repository.save(mapper.userDtoToPerson(userDto));
        log.info("User update");
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Finded user with id: {}", id);
        return mapper.personToUserDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id" + id + " dont exist")));
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
        log.info("User with id {} deleted", id);
    }
}
