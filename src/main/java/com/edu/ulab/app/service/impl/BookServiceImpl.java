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
import java.util.Optional;
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
    public BookDto createBook(BookDto bookDto) {
        Book book = mapper.bookDtoToBook(bookDto);
        book.setPerson(userRepository.findById(bookDto.getUserId()).get());
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return mapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Optional<Book> needed = bookRepository.findAllByPersonId(bookDto.getUserId()).stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle()))
                .findAny();

        needed.ifPresentOrElse(
                book -> {
                    bookDto.setId(book.getId());
                    book = mapper.bookDtoToBook(bookDto);
                    book.setPerson(needed.get().getPerson());
                    bookRepository.save(book);
                    log.info("Updated book: {}", book);
                },
                () -> {
                    createBook(bookDto);
                    log.info("Created new book");
                });
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        log.info("Finded book with id: {}", id);
        return mapper.bookToBookDto(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id" + id + " dont exist")));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
        log.info("Book with id {} deleted", id);
    }

    @Override
    public Set<Long> getAllId() {
        return ((List<Book>) bookRepository.findAll()).stream()
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
