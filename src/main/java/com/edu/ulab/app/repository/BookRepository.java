package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    //    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select b from Book b where b.id = :id")
//    Optional<Book> findByIdForUpdate(long id);
    List<Book> findAllByPersonId(Long userId);

    void deleteAllByPersonId(Long userId);
}
