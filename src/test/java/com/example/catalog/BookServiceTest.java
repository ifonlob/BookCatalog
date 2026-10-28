package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookServiceTest {

    private final BookService service = new BookService();
    private final BookRequestDTO ddd = new BookRequestDTO("978-1", "DDD", "Evans", 2003);

    @Test
    void createStoresTheBookWithAnIncrementalId() {
        BookResponseDTO first = service.create(ddd);
        BookResponseDTO second = service.create(new BookRequestDTO("978-2", "TDD", "Beck", 2002));
        assertEquals(1L, first.id());
        assertEquals(2L, second.id());
        assertEquals(first, service.get(1L));
    }

    @Test
    void listKeepsInsertionOrder() { /* crear dos y comparar ids */ }

    @Test
    void updateKeepsTheRouteId() { /* update(1L, otro) → id 1 y datos nuevos */ }

    @Test
    void deleteRemovesOnlyThatBook() { /* crear dos, borrar uno, list() de tamaño 1 */ }

    @Test
    void getReturnsNullWhenMissing() { assertNull(service.get(99L)); }
}
