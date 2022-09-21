package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.Set;

public interface BookService {
    BookDto createBook(BookDto userDto);

    BookDto updateBook(BookDto userDto);

    BookDto getBookById(Long id);

    void deleteBookById(Long id);

    Set<Long> getAllId();
}
