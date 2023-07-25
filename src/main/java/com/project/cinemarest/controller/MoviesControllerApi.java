package com.project.cinemarest.controller;

import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.model.Statistics;
import com.project.cinemarest.service.MoviesService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cinema/movies")
@RequiredArgsConstructor
public class MoviesControllerApi {

    private final MoviesService moviesService;

    @GetMapping(value = "/list", produces = {"application/json"})
    public ResponseEntity<List<Movie>> getAvailableMoviesList() {
        List<Movie> movieList = moviesService.getAllMovies();
        return ResponseEntity.ok(movieList);
    }

    @GetMapping(value = "/{movie}", produces = {"application/json"})
    public ResponseEntity<Seat> getAllSeatsForMovie(@PathVariable("movie") String movie) {
        return ResponseEntity.of(moviesService.getAvailableSeatsForMovie(movie));
    }

    @GetMapping(value = "/statistics", produces = {"application/json"})
    public List<Statistics> getMoviesStatistics() {
        return Collections.emptyList();
    }

    @GetMapping(value = "/{movie}/info", produces = {"application/json"})
    public ResponseEntity<Movie> getMovieInfo(@PathVariable("movie") String movie) {
        return ResponseEntity.of(moviesService.getMovieInfo(movie));
    }
}
