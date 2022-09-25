package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;
    private final BookRepository repository;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = mapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = repository.save(book);
        log.info("Saved book: {}", savedBook);
        return mapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        List<Book> books = repository.findAllByUserId(bookDto.getUserId());

        Optional<Book> needed = books.stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle())).findAny();

        needed.ifPresentOrElse(
                book -> {
                    bookDto.setId(book.getId());
                    repository.save(mapper.bookDtoToBook(bookDto));
                },
                () -> createBook(bookDto));
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            log.info("Finded book with id: {}", id);
            return mapper.bookToBookDto(book.get());
        }
        log.info("Book with id {} dont exist", id);
        throw new NotFoundException("Book with id" + id + " dont exist");
    }

    @Override
    public void deleteBookById(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            log.info("Deleted book with id: {}", id);
            repository.delete(book.get());
        }
        log.info("Book with id {} dont exist", id);
        throw new NotFoundException("Book with id" + id + " dont exist");

    }

    @Override
    public Set<Long> getAllId() {
        List<Book> books = (List<Book>) repository.findAll();
        return books.stream().map(Book::getId).collect(Collectors.toSet());
    }
}
