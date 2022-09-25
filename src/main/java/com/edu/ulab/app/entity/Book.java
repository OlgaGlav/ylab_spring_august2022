package com.edu.ulab.app.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Book extends AbstractProjectEntity {
    private Long userId;
    private String title;
    private String author;
    private Long pageCount;
}
