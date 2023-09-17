package com.project.cinemarest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetail {

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    private int budget;

    private List<Genre> genres;

    private String homepage;

    private int id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;

    private double popularity;

    @JsonProperty("release_date")
    private String releaseDate;

    private long revenue;

    private int runtime;

    private String status;

    private String tagline;

    private String title;

    private boolean video;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Genre {
        private int id;
        private String name;
    }
}

