package com.project.cinemarest.mapper;


import com.project.cinemarest.entity.Movie;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie mapMovie(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setMovieName(resultSet.getString(1));
        movie.setDirector(resultSet.getString(2));
        movie.setActors(resultSet.getArray(3));
        movie.setDuration(resultSet.getLong(4));
        movie.setAgingRate(resultSet.getInt(5));
        movie.setSummary(resultSet.getString(6));
        return movie;
    }
}
