package com.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookCheckpoint03Test {
    @Autowired MockMvc mvc;
    private String create() throws Exception {
        return mvc.perform(post("/books").contentType("application/json").content("""
                {"isbn":"978-1","title":"Refactoring","author":"Fowler","year":1999}
                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/books/")))
                .andReturn().getResponse().getHeader("Location");
    }
    @Test void createsWithUsableLocation() throws Exception {
        String location = create();
        mvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }
    @Test void deletesOnlyAResourceCreatedByThisTest() throws Exception {
        String location = create();
        mvc.perform(delete(location)).andExpect(status().isNoContent())
                .andExpect(content().string(""));
        mvc.perform(get("/books")).andExpect(jsonPath("$", hasSize(0)));
    }
    @Test void updatesWithRepresentation() throws Exception {
        String location = create();
        mvc.perform(put(location).contentType("application/json").content("""
                {"isbn":"978-1","title":"Refactoring 2","author":"Fowler","year":2018}
                """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.year").value(2018));
        mvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring 2"));
    }
}
