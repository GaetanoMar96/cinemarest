package com.project.cinemarest.service;

import static com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder.eq;

import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder;
import com.project.cinemarest.connector.jpa.MovieRepository;
import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.entity.Show;
import com.project.cinemarest.entity.TCinemaSeat;
import com.project.cinemarest.mapper.MovieMapper;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.repository.QueryJdbcConnector;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private static final String MOVIE_NAME = "M.MOVIE_NAME";
    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;
    private final MovieMapper movieMapper;

    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Show> getAvailableShowsForMovie(String movie) {
        JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getSelectAllShowsByMovie());
        queryBuilder.and(eq(MOVIE_NAME, movie));
        return jdbcConnector.findMany(queryBuilder.build(), Show.class);
    }

    public Optional<Seat> getAvailableSeatsForMovie(String movie, LocalDate date, LocalTime time) {
        JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getSelectAvailableSeatsForMovie());
        queryBuilder.and(eq(MOVIE_NAME, movie));
        queryBuilder.and(eq("START_DATE", date));
        queryBuilder.and(eq("START_TIME", time));
        Optional<TCinemaSeat> optionalSeat = jdbcConnector.findOne(queryBuilder.build(), TCinemaSeat.class);
        return optionalSeat.isPresent() ? movieMapper.mapSeat(optionalSeat.get()) : Optional.empty();
    }

    public Optional<Movie> getMovieInfo(String movie) {
        return movieRepository.findMovieByTitle(movie);
    }
}
