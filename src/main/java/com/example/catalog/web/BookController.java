package com.example.catalog.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/books", produces = MediaType.TEXT_PLAIN_VALUE)
public class BookController {

    @GetMapping
    public String list() { return "GET /books"; }

    @GetMapping("/{id}")
    public String getOne(@PathVariable("id") Long id) { return "GET /books/" + id; }

    @PostMapping
    public String create() { return "POST /books"; }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id) { return "PUT /books/" + id; }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) { return "DELETE /books/" + id; }
}
