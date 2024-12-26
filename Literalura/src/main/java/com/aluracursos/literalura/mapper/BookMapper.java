package com.aluracursos.literalura.mapper;

import com.aluracursos.literalura.dto.BookDTO;
import com.aluracursos.literalura.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setLanguage(book.getLanguage());
        dto.setDownload_count(book.getDownload_count());
        dto.setBirth_year(book.getBirth_year());
        dto.setDeath_year(book.getDeath_year());
        return dto;
    }
    public Book toEntity(BookDTO dto) {
        if (dto == null) {
            return null;
        }
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setLanguage(dto.getLanguage());
        book.setDownload_count(dto.getDownload_count());
        book.setBirth_year(dto.getBirth_year());
        book.setDeath_year(dto.getDeath_year());
        return book;
    }
}

