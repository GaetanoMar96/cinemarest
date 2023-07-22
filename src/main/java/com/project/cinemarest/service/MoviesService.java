package com.project.cinemarest.service;

import static com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder.eq;

import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder;
import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.entity.TCinemaSeat;
import com.project.cinemarest.mapper.MovieMapper;
import com.project.cinemarest.model.Seat;
import com.project.cinemarest.repository.QueryJdbcConnector;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;
    private final MovieMapper movieMapper;

    public List<Movie> getAllMovies() {
        JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getSelectAllMovies());
        return jdbcConnector.findMany(queryBuilder.build(), Movie.class);
    }

    public Optional<Seat> getAvailableSeatsForMovie(String movie) {
        JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getSelectAvailableSeatsForMovie());
        queryBuilder.and(eq("M.MOVIE_NAME", movie));
        Optional<TCinemaSeat> optionalSeat = jdbcConnector.findOne(queryBuilder.build(), TCinemaSeat.class);
        return optionalSeat.isPresent() ? movieMapper.mapSeat(optionalSeat.get()) : Optional.empty();
    }

    public Optional<Movie> getMovieInfo(String movie) {
        JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getSelectMovieInfo());
        queryBuilder.and(eq("M.MOVIE_NAME", movie));
        return jdbcConnector.findOne(queryBuilder.build(), Movie.class);
    }
}
