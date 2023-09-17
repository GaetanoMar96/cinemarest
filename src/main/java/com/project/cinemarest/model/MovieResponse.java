package com.project.cinemarest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {

    @JsonProperty("results")
    private List<Movie> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Movie {

        @JsonProperty("id")
        private int id;

        @JsonProperty("original_language")
        private String originalLanguage;

        @JsonProperty("original_title")
        private String originalTitle;

        @JsonProperty("overview")
        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("title")
        private String title;
    }
}
