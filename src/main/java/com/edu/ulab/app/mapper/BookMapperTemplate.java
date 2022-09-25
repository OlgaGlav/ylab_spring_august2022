package com.edu.ulab.app.mapper;

import com.edu.ulab.app.entity.Book;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapperTemplate implements RowMapper<Book>, Serializable {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("ID"));
        book.setTitle(rs.getString("TITLE"));
        book.setAuthor(rs.getString("AUTHOR"));
        book.setPageCount(rs.getLong("PAGE_COUNT"));
        book.setUserId(rs.getLong("USER_ID"));
        return book;
    }
}