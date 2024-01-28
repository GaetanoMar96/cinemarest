package com.project.cinemarest.controller.cinema;

import com.project.cinemarest.entity.Show;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.service.DataStreamingService;
import com.project.cinemarest.service.MoviesService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cinema/theater")
@RequiredArgsConstructor
public class CinemaControllerApi {

    private final MoviesService moviesService;

    private final DataStreamingService dataStreamingService;

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

    @PostMapping(value = "/stream_movies", produces = {"application/json"})
    public ResponseEntity<Void> streamMovies() {
        return dataStreamingService.streamMoviesIntoDb();
    }
}
