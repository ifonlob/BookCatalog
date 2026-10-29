
package com.example.catalog.service;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.model.Book;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reglas y almacenamiento del catálogo. No conoce tipos HTTP: devuelve DTOs y
 * la colección vive aquí, no en el controlador.
 */
@Service
public class BookService {

    private final Map<Long, Book> books = new LinkedHashMap<>();
    private final AtomicLong ids = new AtomicLong(1);

    public List<BookResponseDTO> list() {
        return books.values().stream().map(this::toDto).toList();
    }

    public BookResponseDTO get(Long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("No existe el libro " + id);
        }
        return toDto(book);
    }


    public BookResponseDTO create(BookRequestDTO request) {
        validate(request);

        Book book = new Book(ids.getAndIncrement(), request.isbn(), request.title(),
                request.author(), request.year());

        books.put(book.getId(), book);
        return toDto(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        if (!books.containsKey(id)) {
            throw new BookNotFoundException("No existe el libro " + id);
        }
        validate(request);

        Book book = new Book(id, request.isbn(), request.title(),
                request.author(), request.year());

        books.put(id, book);
        return toDto(book);
    }

    public void delete(Long id) {
        if (books.remove(id) == null) {
            throw new BookNotFoundException("No existe el libro " + id);
        }
        books.remove(id);
    }

    private void validate(BookRequestDTO request) {
        if (isBlank(request.isbn()) || isBlank(request.title())
                || isBlank(request.author())) {
            throw new InvalidBookException(
                    "isbn, title y author son obligatorios");
        }

        if (request.year() < 0) {
            throw new InvalidBookException("year no puede ser negativo");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private BookResponseDTO toDto(Book book) {
        return new BookResponseDTO(book.getId(), book.getIsbn(), book.getTitle(),
                book.getAuthor(), book.getYear());
    }
}
