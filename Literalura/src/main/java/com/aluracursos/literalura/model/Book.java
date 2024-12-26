package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private String title;
    private String author;
    private String language;
    @Column(name = "download_count")
    private int download_count;
    @Column(name = "birth_year")
    private String birth_year;
    @Column(name = "death_year")
    private String death_year;
    private boolean downloaded; // Nuevo campo *****colocar false

    // Getters y Setters

    public boolean isDownloaded() {    //Nuevo campo
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {  //Nuevo campo
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

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getDeath_year() {
        return death_year;
    }

    public void setDeath_year(String death_year) {
        this.death_year = death_year;
    }
}

