package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Person, Long> {

    void deleteById(Long id);
}
