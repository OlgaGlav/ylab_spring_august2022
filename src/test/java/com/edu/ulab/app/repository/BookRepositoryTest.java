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
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    Person person = new Person();
    Book book = new Book();

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();

        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
    }

    @DisplayName("Сохранить книгу и автора. Число select должно равняться 2")
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
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(0);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Сохранить книгу и автора. Число _________ должно равняться ___")
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
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(0);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу и автора. Число _________ должно равняться ___")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);
        bookRepository.save(book);

        Book updateBook = new Book();
        updateBook.setId(book.getId());
        updateBook.setAuthor("New Test Author");
        updateBook.setTitle("test");
        updateBook.setPageCount(2000);
        updateBook.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(updateBook);

        //Then
        assertThat(result.getAuthor()).isEqualTo("New Test Author");
        assertThat(result.getPageCount()).isEqualTo(2000);
        assertSelectCount(0);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книгу и автора по id. Число _________ должно равняться ___")
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
        assertThat(result.get().getPageCount()).isEqualTo(1000);
        assertSelectCount(0);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить все книг по id автора. Число _________ должно равняться ___")
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
        assertSelectCount(2);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    //todo
    // * failed

}
