package com.project.cinemarest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {

    private List<Movie> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Movie {

        private int id;

        @JsonProperty("original_language")
        private String originalLanguage;

        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("release_date")
        private String releaseDate;

        private String title;
    }
}
