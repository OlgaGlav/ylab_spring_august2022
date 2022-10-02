package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookMapper bookMapper;

    Book book = new Book();
    Book savedBook = new Book();
    Person person = new Person();
    BookDto bookDto = new BookDto();
    BookDto savedDto = new BookDto();

    long ID = 1L;
    long NOT_EXIST_ID = 2L;

    @BeforeEach
    void setUp() {
        person.setId(ID);

        bookDto.setUserId(ID);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        savedDto.setId(ID);
        savedDto.setUserId(ID);
        savedDto.setAuthor("test author");
        savedDto.setTitle("test title");
        savedDto.setPageCount(1000);

        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

    }

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(userRepository.findById(ID)).thenReturn(Optional.of(person));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(savedDto);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(ID, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление существующей книги. Должно пройти успешно.")
    void updateExistBook_Test() {
        when(bookRepository.findAllByPersonId(ID)).thenReturn(Arrays.asList(savedBook));
        when(bookMapper.bookDtoToBook(savedDto)).thenReturn(savedBook);

        assertEquals(bookDto, bookService.updateBook(bookDto));

        verify(bookRepository, times(1)).findAllByPersonId(anyLong());
        verify(bookRepository, times(1)).save(any());
        verify(bookMapper, times(1)).bookDtoToBook(any());
    }

    @Test
    @DisplayName("Обновление существующей книги. Должно пройти успешно.")
    void updateNewBook_Test() {

        when(bookRepository.findAllByPersonId(ID)).thenReturn(new ArrayList<>());

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(userRepository.findById(ID)).thenReturn(Optional.of(person));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(book)).thenReturn(savedDto);

        assertEquals(bookDto, bookService.updateBook(bookDto));

        verify(bookRepository, times(1)).findAllByPersonId(anyLong());
        verify(bookRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(ID);
        verify(bookMapper, times(1)).bookToBookDto(any());
    }

    @Test
    @DisplayName("Получение книги по id. Должно пройти успешно.")
    void getBookById_Test() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(savedBook));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(savedDto);

        assertEquals(savedDto, bookService.getBookById(ID));
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookMapper, times(1)).bookToBookDto(any());
    }

    @Test
    @DisplayName("Получение книги по id. Ошибка. Id не существует.")
    void getPersonById_NegativeTest_NotFoundException() {

        assertThrows(NotFoundException.class, () -> bookService.getBookById(NOT_EXIST_ID));

        assertThatThrownBy(() -> bookService.getBookById(NOT_EXIST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book with id" + NOT_EXIST_ID + " doesn't exist");

        verify(bookRepository, times(2)).findById(anyLong());
        verify(bookMapper, times(0)).bookToBookDto(any());
    }

    @Test
    @DisplayName("Удаление книги по id. Должно пройти успешно.")
    void deleteBookById_Test() {
        bookService.deleteBookById(ID);

        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Получить id всех книг. Должно пройти успешно.")
    void getAllId_Test() {
        Set<Long> ids = new HashSet<>();
        ids.add(ID);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(savedBook));

        assertEquals(ids, bookService.getAllId());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Получить все книги по id user. Должно пройти успешно.")
    void findAllByUserId_Test() {
        when(bookRepository.findAllByPersonId(ID)).thenReturn(Arrays.asList(savedBook));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(savedDto);

        assertEquals(Arrays.asList(savedDto), bookService.findAllByUserId(ID));
        verify(bookRepository, times(1)).findAllByPersonId(anyLong());
    }

    @Test
    @DisplayName("Удаление книги по id user. Должно пройти успешно.")
    void deleteAllBooksByUserId_Test() {
        bookService.deleteAllBooksByUserId(ID);

        verify(bookRepository, times(1)).deleteAllByPersonId(anyLong());
    }
}
