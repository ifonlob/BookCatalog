package com.example.catalog;

import com.example.catalog.dto.*;
import com.example.catalog.service.*;
import com.example.catalog.web.BookController;
import com.example.catalog.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookCheckpoint06Test {
    @Autowired MockMvc mvc;
    @MockitoBean BookService service;
    private static final String VALID = """
            {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
            """;

    @Test void missingBookHasErrorPayload() throws Exception {
        when(service.get(42L)).thenThrow(new BookNotFoundException("Book not found"));
        mvc.perform(get("/books/42")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Book not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
    @Test void serviceRejectionHasErrorPayload() throws Exception {
        when(service.create(any(BookRequestDTO.class)))
                .thenThrow(new InvalidBookException("Book not accepted"));
        mvc.perform(post("/books").contentType("application/json").content(VALID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Book not accepted"));
    }
    @Test void validationHasErrorPayloadWithoutCallingService() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("""
                {"isbn":"978-1","title":" ","author":"Evans","year":2003}
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").isNotEmpty());
        verifyNoInteractions(service);
    }
    @Test void unexpectedFailureDoesNotExposeDetails() throws Exception {
        when(service.get(88L)).thenThrow(new RuntimeException("Internal database detail"));
        mvc.perform(get("/books/88")).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
    @Test void successStillCreatesWithLocation() throws Exception {
        when(service.create(any(BookRequestDTO.class)))
                .thenReturn(new BookResponseDTO(17L, "978-1", "DDD", "Evans", 2003));
        mvc.perform(post("/books").contentType("application/json").content(VALID))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/books/17"));
    }
    @Test void malformedJsonHas400Payload() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
        verifyNoInteractions(service);
    }
}
