package com.example.catalog;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EscrituraPropiaTest {

    @Autowired MockMvc mvc;

    @Test
    void creatingUpdatingAndDeletingKeepsTheSameId() throws Exception {
        MvcResult result = mvc.perform(post("/books").contentType("application/json").content("""
                        {"isbn":"978-84-1","title":"El nombre del viento","author":"Rothfuss","year":2007}
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/books/")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("El nombre del viento"))
                .andReturn();
        String created = result.getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(created, "$.id")).longValue();
        assertEquals("/books/" + id, result.getResponse().getHeader("Location"));

        mvc.perform(get("/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.isbn").value("978-84-1"))
                .andExpect(jsonPath("$.year").value(2007));

        mvc.perform(put("/books/" + id).contentType("application/json").content("""
                        {"isbn":"978-84-2","title":"El temor de un hombre sabio","author":"Rothfuss","year":2011}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.isbn").value("978-84-2"))
                .andExpect(jsonPath("$.title").value("El temor de un hombre sabio"))
                .andExpect(jsonPath("$.year").value(2011));

        mvc.perform(delete("/books/" + id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    static
    class ErroresPropiosTest {

        @Autowired
        MockMvc mvc;

        @Test
        void createRejectsInvalidPayloadWithBadRequest() throws Exception {
            mvc.perform(post("/books")
                            .contentType("application/json")
                            .content("{\"isbn\":\"\",\"title\":\"DDD\",\"author\":\"Evans\",\"year\":2003}"))
                    .andExpect(status().isBadRequest());
        }
    }
}
