package com.edu.ulab.app.entity;

import lombok.Data;

@Data
public class User extends AbstractProjectEntity {
    private String fullName;
    private String title;
    private int age;
}
