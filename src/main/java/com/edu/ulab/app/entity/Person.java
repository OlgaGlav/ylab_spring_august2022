package com.edu.ulab.app.entity;

import lombok.Data;

@Data
public class Person extends AbstractProjectEntity {
    private String fullName;
    private String title;
    private int age;
}
