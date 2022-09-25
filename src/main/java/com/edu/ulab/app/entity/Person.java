package com.edu.ulab.app.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Person extends AbstractProjectEntity {
    private String fullName;
    private String title;
    private int age;
}
