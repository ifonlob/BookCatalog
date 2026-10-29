package com.example.catalog;

import com.example.catalog.service.BookNotFoundException;
import com.example.catalog.service.BookService;
import com.example.catalog.web.BookController;
import com.example.catalog.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class AdvicePropioTest {

    @Autowired MockMvc mvc;
    @MockitoBean BookService service;

    @Test
    void missingBookReturnsNotFoundPayload() throws Exception {
        when(service.get(42L)).thenThrow(new BookNotFoundException("No existe el libro 42"));

        mvc.perform(get("/books/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No existe el libro 42"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}