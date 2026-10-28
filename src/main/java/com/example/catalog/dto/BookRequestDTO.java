package com.example.catalog.dto;

public record BookRequestDTO(String isbn, String title, String author, int year) {
}
