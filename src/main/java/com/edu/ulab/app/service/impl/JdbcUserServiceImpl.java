package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.mapper.UserRowMapper;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class JdbcUserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDto create(UserDto userDto) {

        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE, EMAIL) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> getPreparedStatement(userDto, INSERT_SQL, connection), keyHolder);
        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto update(UserDto userDto) {
        final String UPDATE_SQL = "UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ?, EMAIL = ? WHERE ID = ?";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = getPreparedStatement(userDto, UPDATE_SQL, connection);
                    ps.setLong(5, userDto.getId());
                    return ps;
                });
        log.info("User update");
        return userDto;
    }

    @Override
    public UserDto findById(Long id) {
        log.info("Finded user with id: {}", id);
        String sql = "SELECT * FROM PERSON WHERE ID = " + id;
        try {
            Person person = jdbcTemplate.queryForObject(sql, new UserRowMapper());
            return mapper.personToUserDto(person);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User with id " + id + " doesn't exist.");
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM PERSON WHERE ID = " + id;
        jdbcTemplate.execute(sql);
        log.info("User with id {} deleted", id);
    }

    private PreparedStatement getPreparedStatement(UserDto userDto, String sql, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
        ps.setString(1, userDto.getFullName());
        ps.setString(2, userDto.getTitle());
        ps.setLong(3, userDto.getAge());
        ps.setString(4, userDto.getEmail());
        return ps;
    }
}
