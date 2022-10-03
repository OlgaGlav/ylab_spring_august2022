package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookDto create(BookDto userDto);

    BookDto update(BookDto userDto);

    BookDto findById(Long id);

    void deleteById(Long id);

    Set<Long> getAllId();

    List<BookDto> findAllByUserId(Long userId);

    void deleteAllBooksByUserId(Long userId);

}
