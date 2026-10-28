package com.example.catalog.web;

import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.model.Book;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @GetMapping
    public List<BookResponseDTO> list() { return List.of(); }

    @GetMapping("/{id}")
    public BookResponseDTO getOne(@PathVariable("id") Long id) { return null; }

    @PostMapping
    public String create() { return "POST /books"; }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id) { return "PUT /books/" + id; }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) { return "DELETE /books/" + id; }

    private BookResponseDTO toDto(Book book) {
        return new BookResponseDTO(book.getId(), book.getIsbn(), book.getTitle(),
                book.getAuthor(), book.getYear());
    }
}
