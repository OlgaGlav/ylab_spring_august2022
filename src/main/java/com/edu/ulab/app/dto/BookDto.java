package com.edu.ulab.app.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookDto implements Serializable {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private Long pageCount;
}
