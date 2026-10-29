package com.example.catalog.web;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookNotFoundException;
import com.example.catalog.service.BookService;
import com.example.catalog.service.InvalidBookException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import com.example.catalog.service.InvalidBookException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO request) {
        BookResponseDTO dto = service.create(request);
        return ResponseEntity.created(URI.create("/books/" + dto.id())).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable("id") Long id,
                                                  @RequestBody BookRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<Void> badRequest(InvalidBookException exception) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Void> notFound(BookNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

}