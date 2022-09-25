package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.mapper.UserMapperTemplate;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service("user_template")
public class UserServiceImplTemplate implements UserService {
    private final UserMapper mapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDto createUser(UserDto userDto) {

        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        final String UPDATE_SQL = "UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE ID = ?";
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(UPDATE_SQL);
                        ps.setString(1, userDto.getFullName());
                        ps.setString(2, userDto.getTitle());
                        ps.setInt(3, userDto.getAge());
                        ps.setLong(4, userDto.getId());
                        return ps;
                    }
                });
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        String sql = "SELECT * FROM PERSON WHERE ID = " + id;
        try {
            Person person = jdbcTemplate.queryForObject(sql, new UserMapperTemplate());
            return mapper.personToUserDto(person);
            //todo
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User with id " + id + " dont exist.");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        String sql = "DELETE FROM PERSON WHERE ID = " + id;
        jdbcTemplate.execute(sql);

    }
}
