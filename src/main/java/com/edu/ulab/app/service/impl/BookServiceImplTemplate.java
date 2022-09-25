package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.BookMapperTemplate;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("book_template")
public class BookServiceImplTemplate implements BookService {

    private final BookMapper mapper;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public BookDto createBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK (TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        final String sql = "SELECT * FROM BOOK WHERE USER_ID = ?";
        List<Book> books = jdbcTemplate.query(sql,
                new Object[]{bookDto.getUserId()},
                new BookMapperTemplate());

        Long neededId = books.stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle())).findAny().get().getId();
        bookDto.setId(neededId);
        final String UPDATE_SQL = "UPDATE BOOK SET title = ?, author = ?, page_count = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(UPDATE_SQL);
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        ps.setLong(5, bookDto.getId());
                        return ps;
                    }
                });
        return bookDto;

//        jdbcTemplate.update(UPDATE_SQL, new BookDto[]{bookDto}, new BookMapperTemplate());
//        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        String sql = "SELECT * FROM BOOK WHERE ID = ?";
        return mapper.bookToBookDto(jdbcTemplate.queryForObject(sql, new Object[]{id}, new BookMapperTemplate()));
    }

    @Override
    public void deleteBookById(Long id) {
        String sql = "DELETE FROM BOOK WHERE ID = ?";
        jdbcTemplate.queryForObject(sql, new Object[]{id}, new BookMapperTemplate());

    }

    @Override
    public Set<Long> getAllId() {
        String sql = "SELECT * FROM BOOK";
        List<Book> books = jdbcTemplate.query(sql, new BookMapperTemplate());
        return books.stream().map(Book::getId).collect(Collectors.toSet());
    }
}
