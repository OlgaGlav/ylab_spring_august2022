package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public BookDto create(BookDto bookDto) {
        Book book = mapper.bookDtoToBook(bookDto);
        book.setPerson(userRepository.findById(bookDto.getUserId()).get());
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return mapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto update(BookDto bookDto) {
        bookRepository.findAllByPersonId(bookDto.getUserId()).stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle()))
                .findAny()
                .ifPresentOrElse(
                        book -> update(bookDto, book),
                        () -> {
                            create(bookDto);
                            log.info("Created new book");
                        });
        return bookDto;
    }

    private void update(BookDto bookDto, Book book) {
        bookDto.setId(book.getId());
        Book bookTemp = mapper.bookDtoToBook(bookDto);
        bookTemp.setPerson(book.getPerson());
        bookRepository.save(bookTemp);
        log.info("Updated book: {}", bookTemp);
    }

    @Override
    public BookDto findById(Long id) {
        log.info("Finded book with id: {}", id);
        return mapper.bookToBookDto(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id" + id + " doesn't exist")));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
        log.info("Book with id {} deleted", id);
    }

    @Override
    public Set<Long> getAllId() {
        return (bookRepository.findAll()).stream()
                .map(Book::getId).collect(Collectors.toSet());
    }

    public List<BookDto> findAllByUserId(Long userId) {
        return bookRepository.findAllByPersonId(userId).stream()
                .map(mapper::bookToBookDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllBooksByUserId(Long userId) {
        bookRepository.deleteAllByPersonId(userId);
        log.info("Books with userId {} deleted", userId);
    }
}
