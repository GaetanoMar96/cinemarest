package com.project.cinemarest.entity;

import java.sql.Array;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long idMovie;
    private String movieName;
    private String director;
    @ElementCollection
    private Array actors;
    private Long duration;
    private Integer agingRate;
    private String summary;
}


