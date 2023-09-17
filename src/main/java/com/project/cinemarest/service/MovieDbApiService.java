package com.project.cinemarest.service;

import com.project.cinemarest.connector.rest.movies.GetMovieDetailConnector;
import com.project.cinemarest.connector.rest.movies.GetMoviesConnector;
import com.project.cinemarest.model.MovieDetail;
import com.project.cinemarest.model.MovieResponse.Movie;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieDbApiService {

    private final GetMoviesConnector getMoviesConnector;

    private final GetMovieDetailConnector getMovieDetailConnector;
    public List<Movie> getNowPlayingMovies() {
        List<Movie> movies = getMoviesConnector.getMovies();
        //getting only the first 6 movies
        if (CollectionUtils.size(movies) >= 6) {
            return movies.stream().limit(6).collect(Collectors.toList());
        }
        return movies;
    }

    public MovieDetail getMovieDetail(String movieId) {
        return getMovieDetailConnector.getMovie(movieId);
    }
}
