package com.project.cinemarest.model;

import lombok.Data;

@Data
public class MovieFilters {

    private String title;
    private String yearFrom;
    private String yearTo;
    private String genre;
    private String imdbRating;
}
