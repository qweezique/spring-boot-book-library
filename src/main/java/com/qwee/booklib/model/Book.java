package com.qwee.booklib.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Random;

@Entity
@Data
@Table(name = "book_lib")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Valid
    @NotEmpty(message = "title can't be EMPTY")
    @Column(name = "title")
    private String title;

    @Valid
    @NotEmpty(message = "author can't be EMPTY")
    @Column(name = "author")
    private String author;

    @Column(name = "pages")
    private Integer pagesCount;

    @Column(name = "year")
    private Integer publishYear;

    @Column(name = "price")
    private Double price;

    @Column(name = "ISBN_code")
    private String ISBNCode;

}
