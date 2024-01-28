package com.project.cinemarest.service;

import com.project.cinemarest.connector.jpa.MovieRepository;
import com.project.cinemarest.connector.rest.movies.GetMoviesConnector;
import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.model.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataStreamingService {

    private static final Logger logger = LoggerFactory.getLogger(DataStreamingService.class);

    private final GetMoviesConnector getMoviesConnector;

    private final MovieRepository movieRepository;

    @Transactional
    public ResponseEntity<Void> streamMoviesIntoDb() {
        try {
            List<MovieResponse.Movie> nowPlayingMovies = getMoviesConnector.getMovies();
            List<Movie> movieEntityList = nowPlayingMovies.stream()
                    .limit(8)
                    .map(movie -> {
                        Movie entity = new Movie();
                        entity.setId_movie((long) movie.getId());
                        entity.setMovie_name(movie.getTitle());
                        return entity;
                    }).collect(Collectors.toList());

            movieEntityList.forEach(
                    movieRepository::saveAndFlush
            );
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(RestClientException exception) {
            logger.error("unable to retrieve the movies -> ", exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
