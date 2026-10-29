package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookService;
import com.example.catalog.service.InvalidBookException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void listKeepsInsertionOrder() {
        service.create(ddd);
        service.create(new BookRequestDTO("978-2", "TDD", "Beck", 2002));
        assertEquals(List.of(1L, 2L),
                service.list().stream().map(BookResponseDTO::id).toList());
    }

    @Test
    void updateKeepsTheRouteId() {
        service.create(ddd);
        BookResponseDTO updated = service.update(1L,
                new BookRequestDTO("978-1", "DDD 2.ª ed.", "Evans", 2004));
        assertEquals(1L, updated.id());
        assertEquals("DDD 2.ª ed.", updated.title());
        assertEquals(updated, service.get(1L));
    }

    @Test
    void deleteRemovesOnlyThatBook() {
        service.create(ddd);
        service.create(new BookRequestDTO("978-2", "TDD", "Beck", 2002));
        service.delete(1L);
        assertEquals(1, service.list().size());
        assertNull(service.get(1L));
        assertNotNull(service.get(2L));
    }

    @Test
    void getReturnsNullWhenMissing() {
        assertNull(service.get(99L));
    }

    @Test
    void createRejectsBlankTitle() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", " ", "Evans", 2003)));
    }

    @Test
    void createRejectsNegativeYear() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", "DDD", "Evans", -1)));
    }

    @Test
    void rejectedCreateStoresNothing() {
        assertThrows(InvalidBookException.class,
                () -> service.create(new BookRequestDTO("978-1", "DDD", "Evans", -1)));
        assertTrue(service.list().isEmpty());
    }
}