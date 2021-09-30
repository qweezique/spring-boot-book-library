package com.qwee.booklib.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "book_lib")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;
}
