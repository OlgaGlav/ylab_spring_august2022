package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Person, Long> {

    /*
    User has books - book - started - comited status - other logic
    User has books - book - in progress
    User has books - book - finished
     */

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select p from Person p where p.id = :id")
//    Optional<Person> findByIdForUpdate(long id);
}
