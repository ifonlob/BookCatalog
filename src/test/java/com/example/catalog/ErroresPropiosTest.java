package com.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest @AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ErroresPropiosTest {

    @Autowired MockMvc mvc;

    @Test
    void createWithBlankIsbnIsRejected() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("""
                        {"isbn":"","title":"DDD","author":"Evans","year":2003}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithNegativeYearIsRejected() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("""
                        {"isbn":"978-1","title":"DDD","author":"Evans","year":-1}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectedUpdateLeavesTheStoredBookUntouched() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("""
                        {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
                        """))
                .andExpect(status().isCreated());

        mvc.perform(put("/books/1").contentType("application/json").content("""
                        {"isbn":"978-1","title":" ","author":"Evans","year":2003}
                        """))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDD"));
    }

    @Test
    void getWithUnknownIdIsNotFound() throws Exception {
        mvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithUnknownIdIsNotFound() throws Exception {
        mvc.perform(delete("/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWithUnknownIdIsNotFound() throws Exception {
        mvc.perform(put("/books/999").contentType("application/json").content("""
                        {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
                        """))
                .andExpect(status().isNotFound());
    }
}
