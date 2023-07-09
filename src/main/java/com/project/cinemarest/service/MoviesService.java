package com.project.cinemarest.service;

import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder;
import com.project.cinemarest.entity.Movie;
import com.project.cinemarest.mapper.ResultSetMapper;
import com.project.cinemarest.connector.jdbc.JdbcConnector;
import com.project.cinemarest.repository.QueryJdbcConnector;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;

    public List<Movie> getAllMovies() {
        JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getSelectAllMovies());
        return jdbcConnector.findMany(queryBuilder.build(), Movie.class);
    }
}
