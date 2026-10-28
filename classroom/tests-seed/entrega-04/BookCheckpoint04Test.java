package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookService;
import com.example.catalog.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookCheckpoint04Test {
    @Autowired MockMvc mvc;
    @MockitoBean BookService service;
    private final BookRequestDTO request = new BookRequestDTO("978-1", "DDD", "Evans", 2003);
    private final BookResponseDTO book = new BookResponseDTO(17L, "978-1", "DDD", "Evans", 2003);
    private static final String JSON = """
            {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
            """;

    @Test void delegatesCreateAndUsesReturnedId() throws Exception {
        when(service.create(request)).thenReturn(book);
        mvc.perform(post("/books").contentType("application/json").content(JSON))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/books/17"))
                .andExpect(jsonPath("$.id").value(17));
        verify(service).create(request);
    }
    @Test void delegatesList() throws Exception {
        when(service.list()).thenReturn(List.of(book));
        mvc.perform(get("/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(17));
        verify(service).list();
    }
    @Test void delegatesGet() throws Exception {
        when(service.get(17L)).thenReturn(book);
        mvc.perform(get("/books/17")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDD"));
        verify(service).get(17L);
    }
    @Test void delegatesUpdate() throws Exception {
        when(service.update(17L, request)).thenReturn(book);
        mvc.perform(put("/books/17").contentType("application/json").content(JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(17));
        verify(service).update(17L, request);
    }
    @Test void delegatesDelete() throws Exception {
        mvc.perform(delete("/books/17")).andExpect(status().isNoContent());
        verify(service).delete(17L);
    }
}
