package com.aluracursos.literalura.controller;

import com.aluracursos.literalura.service.BookService;
import com.aluracursos.literalura.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<BookDTO> searchBooks(@RequestParam String title) {
        return bookService.searchBookByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.saveBook(bookDTO));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> listAllBooks() {
        return ResponseEntity.ok(bookService.listAllBooks());
    }

    @GetMapping("/language")
    public ResponseEntity<List<BookDTO>> listBooksByLanguage(@RequestParam String language) {
        return ResponseEntity.ok(bookService.listBooksByLanguage(language));
    }
}
