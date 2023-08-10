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

    @Value("${t_cinema_movie.selectAvailableSeatsForMovie}")
    private String selectAvailableSeatsForMovie;

    @Value("${t_cinema_movie_show.selectAllShowsByMovie}")
    private String selectAllShowsByMovie;

    @Value("${t_cinema_ticket.insertMovieTicket}")
    private String insertMovieTicket;

    @Value("${t_cinema_ticket.deleteMovieTicket}")
    private String deleteMovieTicket;

    @Value("${t_cinema_hall.updateCinemaHall}")
    private String updateCinemaHall;

    @Value("${t_cinema_hall.updateCinemaHallAddingSeat}")
    private String updateCinemaHallAddingSeat;

    @Value("${t_cinema_transaction.deleteTransaction}")
    private String deleteTransaction;

    @Value("${t_cinema_transaction.insertTransaction}")
    private String insertTransaction;

    @Value("${t_cinema_ticket.selectPriceForMovieTicket}")
    private String selectPriceForMovieTicket;
}
