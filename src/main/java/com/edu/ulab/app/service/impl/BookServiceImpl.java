package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static Long id = 0L;

    private final BookMapper mapper;

    private final BookRepository repository;
//    private final BookStorage repository;

    //todo
    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = mapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = repository.save(book);
        log.info("Saved book: {}", savedBook);
        return mapper.bookToBookDto(savedBook);
//        Book book = mapper.bookDtoToBookEntity(bookDto);
//        book.setId(++id);
//        repository.save(book);
//        bookDto.setId(id);
//        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        //todo
        List<Book> books = repository.findByUser(bookDto.getUserId());

        Optional<Book> needed = books.stream()
                .filter(book -> book.getTitle().equals(bookDto.getTitle())).findAny();

        needed.ifPresentOrElse(
                book -> {
                    bookDto.setId(book.getId());
                    repository.save(mapper.bookDtoToBookEntity(bookDto));
                },
                () -> createBook(bookDto));
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        //todo
        Book book = repository.findById(id);
        return mapper.bookEntityToBookDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        //todo
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
