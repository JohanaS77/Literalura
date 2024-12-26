package com.aluracursos.literalura.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    private String name;
    @JsonProperty("birth_year")
    private String birthYear;
    @JsonProperty("death_year")
    private String deathYear;
}
