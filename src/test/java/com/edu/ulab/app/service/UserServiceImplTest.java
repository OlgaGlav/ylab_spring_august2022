package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private final Person person = new Person();
    private final Person savedPerson = new Person();
    private final UserDto userDto = new UserDto();
    private final UserDto result = new UserDto();
    private final long ID = 1L;
    private final long NOT_EXIST_ID = 2L;

    @BeforeEach
    void setUp() {
        int AGE = 11;
        userDto.setAge(AGE);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        person.setFullName("test name");
        person.setAge(AGE);
        person.setTitle("test title");

        savedPerson.setId(ID);
        savedPerson.setFullName("test name");
        savedPerson.setAge(AGE);
        savedPerson.setTitle("test title");

        result.setId(ID);
        result.setAge(AGE);
        result.setFullName("test name");
        result.setTitle("test title");
    }

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then
        UserDto userDtoResult = userService.create(userDto);
        assertEquals(ID, userDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(userDto);

        assertEquals(userDto, userService.update(userDto));
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).userDtoToPerson(any());
    }

    @Test
    @DisplayName("Поиск пользователя по id. Должно пройти успешно.")
    void getPersonById_Test() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(savedPerson));
        when(userMapper.personToUserDto(savedPerson)).thenReturn(userDto);

        assertEquals(userDto, userService.findById(ID));
        verify(userRepository, times(1)).findById(ID);
        verify(userMapper, times(1)).personToUserDto(any());
    }

    @Test
    @DisplayName("Поиск пользователя по id. Ошибка. Id не существует.")
    void getPersonById_NegativeTest_NotFoundException() {

        assertThrows(NotFoundException.class, () -> userService.findById(2L));

        assertThatThrownBy(() -> userService.findById(NOT_EXIST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with id" + NOT_EXIST_ID + " doesn't exist");

        verify(userRepository, times(2)).findById(anyLong());
        verify(userMapper, times(0)).personToUserDto(any());
    }

    @Test
    @DisplayName("Удаление пользователя по id. Должно пройти успешно.")
    void deletePersonById_Test() {
        userService.deleteById(ID);

        verify(userRepository, times(1)).deleteById(ID);
    }
}
