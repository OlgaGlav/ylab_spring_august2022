package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
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
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {

    private final UserRepository userRepository;
    private final Person person = new Person();
    private final long ID = 1L;
    private final int AGE = 111;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
        person.setAge(AGE);
        person.setTitle("reader");
        person.setFullName("Test Test");
    }

    @DisplayName("Сохранить юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(AGE);
        checkAssertCount(0, 1, 0, 0);
    }

    @DisplayName("Обновить юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        userRepository.save(person);
        //Given
        Person savedPerson = person;
        savedPerson.setAge(222);
        savedPerson.setTitle("new reader");
        savedPerson.setFullName("Test Test");

        //When
        Person result = userRepository.save(savedPerson);

        //Then
        assertThat(result.getId()).isEqualTo(person.getId());
        assertThat(result.getAge()).isEqualTo(222);
        checkAssertCount(0, 1, 0, 0);
    }

    @DisplayName("Найти юзера по id.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getUserById_thenAssertDmlCount() {
        //Given
        userRepository.save(person);

        //When
        Optional<Person> result = userRepository.findById(person.getId());
        //Then
        assertThat(person.equals(result.get()));
        checkAssertCount(0, 1, 0, 0);
    }

    @DisplayName("Удалить юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePersonById_thenAssertDmlCount() {
        userRepository.save(person);

        //When
        userRepository.deleteById(person.getId());
        //Then
        checkAssertCount(0, 1, 0, 0);
    }

    private void checkAssertCount(int select, int insert, int update, int delete) {
        assertSelectCount(select);
        assertInsertCount(insert);
        assertUpdateCount(update);
        assertDeleteCount(delete);
    }
}
