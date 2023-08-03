package com.project.cinemarest.entity;

import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "movies")
public class Movie {

    @Id
    private String id;
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String actors;
    private String plot;
    private String poster;
    private String imdbRating;
}


