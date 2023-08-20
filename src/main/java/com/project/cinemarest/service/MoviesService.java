package com.project.cinemarest.service;

import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.OperatorEnum;
import com.project.cinemarest.connector.jpa.mongo.MovieRepository;
import com.project.cinemarest.connector.mongo.MongoSpecification.Operation;
import com.project.cinemarest.connector.mongo.MongoSpecificationsBuilder;
import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.entity.Show;
import com.project.cinemarest.entity.Hall;
import com.project.cinemarest.mapper.MovieMapper;
import com.project.cinemarest.model.MovieFilters;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.connector.jdbc.QueryJdbcConnector;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private static final String MOVIE_NAME = "M.MOVIE_NAME";
    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;
    private final MovieMapper movieMapper;

    private final MovieRepository movieRepository;

    //private final MovieSpecificationRepository movieSpecificationRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Show> getAvailableShowsForMovie(String movie) {
        JdbcQuery jdbcQuery = new JdbcQuery(jdbcQueryMovie.getSelectAllShowsByMovie());
        jdbcQuery.eq(OperatorEnum.AND, MOVIE_NAME, movie);
        return jdbcConnector.findMany(jdbcQuery, Show.class);
    }

    public Optional<Seat> getAvailableSeatsForMovie(String movie, LocalDate date, LocalTime time) {
        JdbcQuery jdbcQuery = new JdbcQuery(jdbcQueryMovie.getSelectAvailableSeatsForMovie());
        jdbcQuery.eq(OperatorEnum.AND, MOVIE_NAME, movie);
        jdbcQuery.eq(OperatorEnum.AND, "START_DATE", date);
        jdbcQuery.eq(OperatorEnum.AND, "START_TIME", time);
        Optional<Hall> optionalSeat = jdbcConnector.findOne(jdbcQuery, Hall.class);
        return optionalSeat.isPresent() ? movieMapper.mapSeat(optionalSeat.get()) : Optional.empty();
    }

    public Optional<Movie> getMovieInfo(String movie) {
        return movieRepository.findMovieByTitle(movie);
    }

    public List<Movie> findMoviesWithFilters(MovieFilters filters) {
        MongoSpecificationsBuilder<Movie> builder = new MongoSpecificationsBuilder<>();
        if (filters.getTitle() != null) {
            builder.with("title", Operation.EQ, filters.getTitle());
        }
        if (filters.getGenre() != null) {
            builder.with("genre", Operation.RGX, filters.getGenre());
        }
        if (filters.getImdbRating() != null) {
            builder.with("imdbRating", Operation.LTE, filters.getImdbRating());
        }
        if (filters.getYearFrom() != null && filters.getYearTo() != null) {
            builder.with("year", Operation.GTE, filters.getYearFrom());
            builder.with("year", Operation.LTE, filters.getYearTo());
        } else if (filters.getYearFrom() != null) {
            builder.with("year", Operation.GTE, filters.getYearFrom());
        } else if (filters.getYearTo() != null) {
            builder.with("year", Operation.LTE, filters.getYearTo());
        }

        Specification<Movie> spec = builder.build();
        //return movieSpecificationRepository.findAll(spec);
        return null;
    }
}
