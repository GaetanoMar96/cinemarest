package com.project.cinemarest.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_cinema_movie")
public class Movie {

    @Id
    private Long id_movie;
    private String movie_name;
}


