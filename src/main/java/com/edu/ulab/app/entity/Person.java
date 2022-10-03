package com.edu.ulab.app.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "person")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 255)
    private String fullName;

    @Size(min = 2, max = 255)
    private String title;

    private int age;

    @Email
    private String email;

    @OneToMany(mappedBy = "person")
    private List<Book> books;
}
