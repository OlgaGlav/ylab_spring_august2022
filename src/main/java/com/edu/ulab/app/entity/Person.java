package com.edu.ulab.app.entity;

import lombok.Data;

import javax.persistence.*;
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

    //todo
    //    @Column(nullable = false)
    //    private int count;
    private int age;

    @OneToMany(mappedBy = "person")
    private List<Book> books;
}
