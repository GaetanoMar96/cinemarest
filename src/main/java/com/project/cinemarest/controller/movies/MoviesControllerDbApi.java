package com.project.cinemarest.controller.movies;

import com.project.cinemarest.model.MovieDetail;
import com.project.cinemarest.model.MovieResponse.Movie;
import com.project.cinemarest.service.MovieDbApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cinema/movies/db")
@RequiredArgsConstructor
public class MoviesControllerDbApi {

    private final MovieDbApiService movieDbApiService;

    @GetMapping(value = "/now_playing", produces = {"application/json"})
    public ResponseEntity<List<Movie>> getNowPlayingMovies() {
        return ResponseEntity.ok(movieDbApiService.getNowPlayingMovies());
    }

    @GetMapping(value = "/movie/{movie_id}", produces = {"application/json"})
    public ResponseEntity<MovieDetail> getMovieDetail(@PathVariable("movie_id") String movieId) {
        return ResponseEntity.ok(movieDbApiService.getMovieDetail(movieId));
    }

    @GetMapping(value = "/upcoming", produces = {"application/json"})
    public ResponseEntity<List<Movie>> getUpcomingMovies() {
        return ResponseEntity.ok(movieDbApiService.getUpcomingMovies());
    }
}