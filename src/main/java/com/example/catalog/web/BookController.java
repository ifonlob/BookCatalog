package com.example.catalog.web;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.model.Book;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
public class BookController {

    private final Map<Long, Book> books = new LinkedHashMap<>();
    private final AtomicLong ids = new AtomicLong(1);

    @GetMapping
    public List<BookResponseDTO> list() {
        return books.values().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public BookResponseDTO getOne(@PathVariable("id") Long id) {
        Book book = books.get(id);
        return book == null ? null : toDto(book);
    }

    @PostMapping
    public BookResponseDTO create(@RequestBody BookRequestDTO request) {
        Book book = new Book(ids.getAndIncrement(), request.isbn(), request.title(), request.author(), request.year());
        books.put(book.getId(), book);
        return toDto(book);
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(@PathVariable("id") Long id, @RequestBody BookRequestDTO request) {
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toDto(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        books.remove(id);
    }

    private BookResponseDTO toDto(Book book) {
        return new BookResponseDTO(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getYear());
    }
}
