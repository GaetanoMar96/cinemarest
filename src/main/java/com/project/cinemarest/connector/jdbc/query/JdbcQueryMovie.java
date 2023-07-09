package com.project.cinemarest.connector.jdbc.query;

import com.project.cinemarest.factory.YamlPropertySourceFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource(value = "classpath:query/query_movie.yaml", factory = YamlPropertySourceFactory.class)
public class JdbcQueryMovie {

    @Value("${t_cinema_movie.selectAllMovies}")
    private String selectAllMovies;

    @Value("${t_cinema_movie.selectMovieInfo}")
    private String selectMovieInfo;


}
