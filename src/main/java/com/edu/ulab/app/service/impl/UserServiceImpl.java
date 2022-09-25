package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static Long id = 0L;

    //    private final UserStorage repository;
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        //todo
        Person person = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", person);
        Person savedUser = userRepository.save(person);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
//        User user = mapper.userDtoToUserEntity(userDto);
//        user.setId(++id);
//        repository.save(user);
//        userDto.setId(id);
//        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        //todo
        repository.save(mapper.userDtoToPerson(userDto));
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        //todo
        Person person = repository.findById(id);
        return mapper.PersonToUserDto(person);
    }

    @Override
    public void deleteUserById(Long id) {

        //todo
        repository.delete(id);
    }
}
