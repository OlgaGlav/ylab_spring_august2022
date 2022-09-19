package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dtomapper.BookEntityMapper;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static Long id = 0L;

    private final BookEntityMapper mapper;

    private final BookStorage repository;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = mapper.bookDtoToBookEntity(bookDto);
        book.setId(++id);
        repository.save(book);
        bookDto.setId(id);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        List<Book> books = repository.findByUser(bookDto.getUserId());
        for (Book b : books) {
            if (b.getTitle().equals(bookDto.getTitle())) {
                bookDto.setId(b.getId());
            }
        }
        repository.save(mapper.bookDtoToBookEntity(bookDto));
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = repository.findById(id);
        return mapper.bookEntityToBookDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = repository.findById(id);
        if (book.getUserId() == null) {
            repository.delete(id);
        }
    }

    @Override
    public Set<Long> getAllId() {
        return repository.getAllId();
    }
}
