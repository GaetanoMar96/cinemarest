package com.project.cinemarest.controller.movies;

import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.model.MovieFilters;
import com.project.cinemarest.service.MoviesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cinema/movies")
@RequiredArgsConstructor
public class MoviesControllerApi {

    private final MoviesService moviesService;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<Movie>> getAvailableMoviesList() {
        return ResponseEntity.ok(moviesService.getAllMovies());
    }

    @GetMapping(value = "/{movie}/info", produces = {"application/json"})
    public ResponseEntity<Movie> getMovieInfo(@PathVariable("movie") String movie) {
        return ResponseEntity.of(moviesService.getMovieInfo(movie));
    }

    @PostMapping(value = "/filters", produces = {"application/json"})
    public ResponseEntity<List<Movie>> getMoviesByFilters(
        @RequestBody MovieFilters request) {
        return ResponseEntity.ok(moviesService.findMoviesWithFilters(request));
    }
}
