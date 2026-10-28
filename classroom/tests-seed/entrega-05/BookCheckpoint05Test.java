package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.service.BookService;
import com.example.catalog.service.BookNotFoundException;
import com.example.catalog.service.InvalidBookException;
import com.example.catalog.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookCheckpoint05Test {
    @Autowired MockMvc mvc;
    @MockitoBean BookService service;

    @Test void mapsMissingBookTo404() throws Exception {
        when(service.get(99L)).thenThrow(new BookNotFoundException("Book not found"));
        mvc.perform(get("/books/99")).andExpect(status().isNotFound());
    }
    @Test void mapsServiceRejectionTo400() throws Exception {
        var request = new BookRequestDTO("978-1", "DDD", "Evans", 2003);
        when(service.create(request)).thenThrow(new InvalidBookException("Book not accepted"));
        mvc.perform(post("/books").contentType("application/json").content("""
                {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
                """))
                .andExpect(status().isBadRequest());
        verify(service).create(request);
    }
    @Test void mapsMissingDeleteTo404() throws Exception {
        doThrow(new BookNotFoundException("Book not found")).when(service).delete(99L);
        mvc.perform(delete("/books/99")).andExpect(status().isNotFound());
    }
}
