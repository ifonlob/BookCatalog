package com.example.catalog;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookCheckpoint02Test {
    @Autowired MockMvc mvc;
    private static final String BOOK = """
            {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
            """;
    private long create() throws Exception {
        String json = mvc.perform(post("/books").contentType("application/json").content(BOOK))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("DDD"))
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }
    @Test void startsWithEmptyJsonList() throws Exception {
        mvc.perform(get("/books").accept("application/json"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
    @Test void createsAndRetrievesJson() throws Exception {
        long id = create();
        mvc.perform(get("/books/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.isbn").value("978-1"))
                .andExpect(jsonPath("$.author").value("Evans"))
                .andExpect(jsonPath("$.year").value(2003));
    }
    @Test void updatesAndDeletesInMemory() throws Exception {
        long id = create();
        mvc.perform(put("/books/" + id).contentType("application/json").content("""
                {"isbn":"978-1","title":"Updated","author":"Evans","year":2004}
                """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Updated"));
        mvc.perform(delete("/books/" + id)).andExpect(status().isOk());
        mvc.perform(get("/books")).andExpect(jsonPath("$", hasSize(0)));
    }
}
