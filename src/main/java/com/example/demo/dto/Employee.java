package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "employees")
public class Employee {

    @Id
    private long id;

    @Indexed(unique = true)
    private String name;
    private String state;
    private int age;
    private String contract;
}


