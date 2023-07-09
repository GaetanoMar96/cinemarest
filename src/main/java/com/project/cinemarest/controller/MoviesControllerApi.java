package com.project.cinemarest.controller;

import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.model.Statistics;
import com.project.cinemarest.service.MoviesService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MoviesControllerApi {

    private final MoviesService moviesService;

    @GetMapping(value = "/api/cinema/movies", produces = {"application/json"})
    public ResponseEntity<List<Movie>> getAvailableMoviesList() {
        List<Movie> movieList = moviesService.getAllMovies();
        return ResponseEntity.ok(movieList);
    }

    @GetMapping(value = "/api/cinema/movies/{movie}", produces = {"application/json"})
    public List<Seat> getAllSeatsForMovie(@PathVariable("movie") String movie) {
        return Collections.emptyList();
    }

    @GetMapping(value = "/api/cinema/movies/statistics", produces = {"application/json"})
    public List<Statistics> getMoviesStatistics() {
        return Collections.emptyList();
    }

    @GetMapping(value = "/api/cinema/movies/{movie}/info", produces = {"application/json"})
    public ResponseEntity<Movie> getMovieInfo(@PathVariable("movie") String movie) {
        Optional<Movie> optionalMovie = Optional.of(new Movie());
        return ResponseEntity.of(optionalMovie);
    }
}
