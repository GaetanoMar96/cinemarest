package com.project.cinemarest.mapper;


import com.project.cinemarest.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResultSetMapper {

    private static final Logger logger = LoggerFactory.getLogger(ResultSetMapper.class);

    private final MovieMapper movieMapper;

    public <T> List<T> map(final ResultSet resultSet, final Class<T> destinationClass) {
        final List<T> list = new ArrayList<>();
        try {
            if (resultSet == null || !resultSet.isBeforeFirst()) {
                logger.warn("An empty ResultSet has been passed in! Empty list will be returned.");
                return list;
            }

            while (resultSet.next()) {
                logger.trace("Adding new {} to the list", destinationClass);
                list.add(createObject(resultSet, destinationClass));
            }
        } catch (SQLException ex) {
            logger.error("Something has gone wrong while mapping! Exception: {}", ex.getMessage());
        }
        logger.info("ResultSet has been successfully mapped to {}", destinationClass);
        return list;
    }

    private <T> T createObject(ResultSet resultSet, Class<T> destinationClass) throws SQLException {
        if (destinationClass.isInstance(Movie.class)) {
            return mapMovie(resultSet);
        }
        return null;
    }

    private <T> T mapMovie(ResultSet resultSet) throws SQLException {
        return (T) movieMapper.mapMovie(resultSet);
    }
}
