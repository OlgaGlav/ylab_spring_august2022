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
        List<Book> books = bookRepository.findAllByPersonId(bookDto.getUserId());
        Optional<Book> needed = books.stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle())).findAny();

        needed.ifPresentOrElse(
                book -> {
                    bookDto.setId(book.getId());
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
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            log.info("Finded book with id: {}", id);

            return mapper.bookToBookDto(book.get());
        }
        log.info("Book with id {} dont exist", id);
        throw new NotFoundException("Book with id" + id + " dont exist");
    }

    @Override
    public void deleteBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            log.info("Deleted book with id: {}", id);
            bookRepository.delete(book.get());
        }
        log.info("Book with id {} dont exist", id);
        throw new NotFoundException("Book with id" + id + " dont exist");

    }

    @Override
    public Set<Long> getAllId() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        return books.stream().map(Book::getId).collect(Collectors.toSet());
    }

    public List<BookDto> findAllByUserId(Long userId) {
        return bookRepository.findAllByPersonId(userId).stream()
                .map(mapper::bookToBookDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBookByUserId(Long userId) {
        bookRepository.deleteAllByPersonId(userId);
    }
}
