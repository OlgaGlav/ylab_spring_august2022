package com.edu.ulab.app.entity;

import lombok.Data;

@Data
public class Book extends AbstractProjectEntity {
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}
