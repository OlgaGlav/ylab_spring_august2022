package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Person;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component(value = "user_repository")
@Primary
public class UserStorage extends AbstractStorage<Person> {

}
