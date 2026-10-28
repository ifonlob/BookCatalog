package com.example.catalog;

import com.example.catalog.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookCheckpoint01Test {

    @Autowired MockMvc mvc;

    @Test
    void echo_get_books() throws Exception {
        mvc.perform(get("/books"))
           .andExpect(status().isOk())
           .andExpect(content().string("GET /books"));
    }

    @Test void echo_get_one() throws Exception {
        mvc.perform(get("/books/42")).andExpect(status().isOk())
                .andExpect(content().string("GET /books/42"));
    }
    @Test void echo_post() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/books"))
                .andExpect(status().isOk()).andExpect(content().string("POST /books"));
    }
    @Test void echo_put() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/books/42"))
                .andExpect(status().isOk()).andExpect(content().string("PUT /books/42"));
    }
    @Test void echo_delete() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/books/42"))
                .andExpect(status().isOk()).andExpect(content().string("DELETE /books/42"));
    }
}
