package com.example.catalog.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private int year;

}