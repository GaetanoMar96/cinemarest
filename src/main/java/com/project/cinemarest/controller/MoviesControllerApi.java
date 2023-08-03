package com.project.cinemarest.controller;

import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.entity.Show;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.model.Statistics;
import com.project.cinemarest.service.MoviesService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping(value = "/all", produces = {"application/json"})
    public ResponseEntity<List<Movie>> getAvailableMoviesList() {
        return ResponseEntity.ok(moviesService.getAllMovies());
    }

    @GetMapping(value = "/{movie}/shows", produces = {"application/json"})
    public ResponseEntity<List<Show>> getAllShowsForMovie(@PathVariable("movie") String movie) {
        return ResponseEntity.ok(moviesService.getAvailableShowsForMovie(movie));
    }

    @GetMapping(value = "/{movie}/seats/{date}/{time}", produces = {"application/json"})
    public ResponseEntity<Seat> getAllSeatsForMovie(@PathVariable("movie") String movie,
        @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @PathVariable("time") @DateTimeFormat(pattern = "HH:mm") LocalTime time) {
        return ResponseEntity.of(moviesService.getAvailableSeatsForMovie(movie, date, time));
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
