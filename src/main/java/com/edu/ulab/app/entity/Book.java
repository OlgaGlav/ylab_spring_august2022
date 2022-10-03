package com.edu.ulab.app.entity;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "person_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person person;
    @Size(min = 2, max = 255)
    private String title;
    @Size(min = 2, max = 255)
    private String author;
    private long pageCount;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", personId=" + person.getId() +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pageCount=" + pageCount +
                '}';
    }
}
