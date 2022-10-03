package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.BookRowMapper;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class JdbcBookServiceImpl implements BookService {

    private final BookMapper mapper;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public BookDto create(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK (TITLE, AUTHOR, PAGE_COUNT, PERSON_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> getPreparedStatement(bookDto, INSERT_SQL, connection), keyHolder);
        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bookDto;
    }

    @Override
    public BookDto update(BookDto bookDto) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM BOOK WHERE PERSON_ID = ?",
                new BookRowMapper(),
                bookDto.getUserId());

        books.stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle()))
                .findAny()
                .ifPresentOrElse(
                        book -> {
                            bookDto.setId(book.getId());
                            log.info("Id needed for update book find by title {}", book.getId());
                            final String UPDATE_SQL = "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PAGE_COUNT = ?, PERSON_ID = ? WHERE id = ?";
                            jdbcTemplate.update(
                                    connection -> {
                                        PreparedStatement ps = getPreparedStatement(bookDto, UPDATE_SQL, connection);
                                        ps.setLong(5, bookDto.getId());
                                        return ps;
                                    });
                            log.info("Updated book: {}", book);
                        },
                        () -> {
                            create(bookDto);
                            log.info("Created new book");
                        });

        return bookDto;
    }

    @Override
    public BookDto findById(Long id) {
        log.info("Finded book with id: {}", id);
        String sql = "SELECT * FROM BOOK WHERE ID = ?";
        return mapper.bookToBookDto(jdbcTemplate.queryForObject(sql, new BookRowMapper(), id));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM BOOK WHERE ID = ?";
        jdbcTemplate.queryForObject(sql, new BookRowMapper(), id);
        log.info("Book with id {} deleted", id);
    }

    @Override
    public Set<Long> getAllId() {
        String sql = "SELECT * FROM BOOK";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());
        return books.stream().map(Book::getId).collect(Collectors.toSet());
    }

    @Override
    public List<BookDto> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM BOOK b WHERE PERSON_ID =" + userId;
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());
        return books.stream().map(mapper::bookToBookDto).collect(Collectors.toList());
    }

    @Override
    public void deleteAllBooksByUserId(Long userId) {
        String sql = "DELETE FROM BOOK b WHERE PERSON_ID =" + userId;
        jdbcTemplate.execute(sql);
        log.info("Books with userId {} deleted", userId);
    }

    private PreparedStatement getPreparedStatement(BookDto bookDto, String sql, Connection connection) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(sql, new String[]{"id"});
        ps.setString(1, bookDto.getTitle());
        ps.setString(2, bookDto.getAuthor());
        ps.setLong(3, bookDto.getPageCount());
        ps.setLong(4, bookDto.getUserId());
        return ps;
    }
}
