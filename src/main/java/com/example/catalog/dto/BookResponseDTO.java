package com.example.catalog.dto;

public record BookResponseDTO(Long id, String isbn, String title, String author, int year) {}