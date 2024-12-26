package com.aluracursos.literalura.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private int count;
    private String next;
    private String previous;
    private List<BookDTO> results;

    // Getters y Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<BookDTO> getResults() {
        return results;
    }

    public void setResults(List<BookDTO> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BookDTO {
        private int id;
        private String title;
        private List<Author> authors;
        private List<String> languages;
        @JsonProperty("download_count")
        private int downloadCount;

        // Getters y Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Author> getAuthors() {
            return authors;
        }

        public void setAuthors(List<Author> authors) {
            this.authors = authors;
        }

        public List<String> getLanguages() {
            return languages;
        }

        public void setLanguages(List<String> languages) {
            this.languages = languages;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Author {
            private String name;
            @JsonProperty("birth_year")
            private String birthYear;
            @JsonProperty("death_year")
            private String deathYear;

            // Getters y Setters
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBirthYear() {
                return birthYear;
            }

            public void setBirthYear(String birthYear) {
                this.birthYear = birthYear;
            }

            public String getDeathYear() {
                return deathYear;
            }

            public void setDeathYear(String deathYear) {
                this.deathYear = deathYear;
            }
        }
    }
}
