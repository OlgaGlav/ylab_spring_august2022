package com.edu.ulab.app.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String fullName;
    private String title;
    private int age;
}
