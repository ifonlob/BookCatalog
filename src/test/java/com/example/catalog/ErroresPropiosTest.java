package com.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Recorrido por HTTP de los errores propios de la entrega 5: la entrada
 * inválida se rechaza con 400 sin llegar a guardar nada.
 */
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
}
