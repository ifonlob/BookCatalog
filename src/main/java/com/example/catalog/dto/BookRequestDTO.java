package com.example.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequestDTO(
        @NotBlank(message = "isbn es obligatorio") String isbn,
        @NotBlank(message = "title es obligatorio") String title,
        @NotBlank(message = "author es obligatorio") String author,
        @Min(value = 0, message = "year no puede ser negativo") int year) {
}
