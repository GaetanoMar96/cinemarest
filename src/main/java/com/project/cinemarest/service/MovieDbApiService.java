package com.project.cinemarest.service;

import com.project.cinemarest.cache.MoviesCache;
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

    private final MoviesCache moviesCache = MoviesCache.getInstance();

    public List<Movie> getNowPlayingMovies() {
        List<Movie> movies = moviesCache.getFromCache("NOW");
        if (CollectionUtils.isNotEmpty(movies)) {
            return movies;
        }

        movies = getMoviesConnector.getMovies();

        //getting only the first 4 movies
        movies = movies.stream().limit(4).collect(Collectors.toList());
        moviesCache.addToCache("NOW", movies);
        return movies;
    }

    public MovieDetail getMovieDetail(String movieId) {
        return getMovieDetailConnector.getMovie(movieId);
    }

    public List<Movie> getUpcomingMovies() {
        List<Movie> movies = moviesCache.getFromCache("UPCOMING");
        if (CollectionUtils.isNotEmpty(movies)) {
            return movies;
        }

        movies = getMoviesConnector.getUpcomingMovies();

        //getting only the first 4 movies
        movies = movies.stream().limit(4).collect(Collectors.toList());
        moviesCache.addToCache("UPCOMING", movies);
        return movies;
    }
}
