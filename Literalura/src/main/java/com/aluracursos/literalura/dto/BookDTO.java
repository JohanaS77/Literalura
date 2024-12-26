package com.aluracursos.literalura.dto;

import java.util.List;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String language;
    private int download_count;
    private String birth_year;
    private String death_year;
    private List<String> booksByAuthor;
    private boolean downloaded; // Nuevo campo

    // Getters y Setters

    public boolean isDownloaded() {    // Nuevo campo
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {    // Nuevo campo
        this.downloaded = downloaded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDownload_count() {
        return download_count;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        //this.birth_year = String.valueOf(birth_year);
        this.birth_year = (birth_year == null || birth_year.isEmpty())
                ? "No disponible"
                : birth_year;
    }

    public String getDeath_year() {
        return death_year;
    }

    public void setDeath_year(String death_year) {
        //this.death_year = death_year;
        this.death_year = (death_year == null || death_year.isEmpty())
                ? "No disponible"
                : death_year;
    }

    public List<String> getBooksByAuthor() {
        return booksByAuthor;
    }

    public void setBooksByAuthor(List<String> booksByAuthor) {
        this.booksByAuthor = booksByAuthor;
    }

    public void setDeathYear(Object deathYear) {
        if (deathYear != null) {
            this.death_year = deathYear.toString();
        }
    }

    public String getDownloadCount() {
        return String.valueOf(download_count);
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }
}

