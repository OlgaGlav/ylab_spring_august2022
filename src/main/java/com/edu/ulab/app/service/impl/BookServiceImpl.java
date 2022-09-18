package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.AbstractStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static Long id = 1L;
    @Qualifier("book_storage")
    private final AbstractStorage storage;

    @Override
    public BookDto createBook(BookDto bookDto) {
        //todo
        bookDto.setId(id++);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        //todo
        return null;
    }

    @Override
    public BookDto getBookById(Long id) {
        //todo
        return null;
    }

    @Override
    public void deleteBookById(Long id) {
        //todo
    }

    @Override
    public Set<Long> getAllId() {
        return storage.getAllId();
    }

}
