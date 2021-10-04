package com.qwee.booklib.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchCriteria {

    private String author;
    private String title;
    private Integer pagesCount;
    private Integer publishYear;
    private Double price;
    private String ISBNCode;

}
