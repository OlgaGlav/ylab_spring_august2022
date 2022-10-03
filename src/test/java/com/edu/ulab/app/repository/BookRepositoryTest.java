package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final Person person = new Person();
    private final Book book = new Book();
    private final long PAGE_COUNT = 1000L;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository,
                              UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();

        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(PAGE_COUNT);
    }

    @DisplayName("Сохранить книгу и автора.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void findAllBadges_thenAssertDmlCount() {
        //Given
        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(PAGE_COUNT);
        assertThat(result.getTitle()).isEqualTo("test");
        checkAssertCount(0, 2, 0, 0);
    }

    @DisplayName("Сохранить книгу и автора.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void createBook_thenAssertDmlCount() {
        //Given
        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(PAGE_COUNT);
        assertThat(result.getTitle()).isEqualTo("test");
        checkAssertCount(0, 2, 0, 0);
    }

    @DisplayName("Обновить книгу и автора.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        long newPageCount = 2000L;
        //Given
        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);
        bookRepository.save(book);

        Book updateBook = new Book();
        updateBook.setId(book.getId());
        updateBook.setAuthor("New Test Author");
        updateBook.setTitle("test");
        updateBook.setPageCount(newPageCount);
        updateBook.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(updateBook);

        //Then
        assertThat(result.getAuthor()).isEqualTo("New Test Author");
        assertThat(result.getPageCount()).isEqualTo(newPageCount);
        checkAssertCount(0, 2, 0, 0);
    }

    @DisplayName("Получить книгу и автора по id.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBookById_thenAssertDmlCount() {
        //Given
        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);
        Book savedBook = bookRepository.save(book);

        //When
        Optional<Book> result = bookRepository.findById(savedBook.getId());

        //Then
        assertThat(result.get().getAuthor()).isEqualTo("Test Author");
        assertThat(result.get().getPageCount()).isEqualTo(PAGE_COUNT);
        checkAssertCount(0, 2, 0, 0);
    }

    @DisplayName("Удалить все книг по id автора.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteAllBooksByUserId_thenAssertDmlCount() {
        //Given
        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);
        bookRepository.save(book);

        //When
        bookRepository.deleteAllByPersonId(person.getId());

        //Then
        assertThat(bookRepository.findAllByPersonId(person.getId()) == null);
        checkAssertCount(2, 2, 0, 1);
    }

    private void checkAssertCount(int select, int insert, int update, int delete) {
        assertSelectCount(select);
        assertInsertCount(insert);
        assertUpdateCount(update);
        assertDeleteCount(delete);
    }
}
