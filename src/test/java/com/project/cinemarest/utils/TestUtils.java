package com.project.cinemarest.utils;

import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.model.MovieDetail;
import com.project.cinemarest.model.MovieResponse.Movie;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static ClientInfo createClient() {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUserId(UUID.fromString("41e822f6-d648-4a1c-acc3-44c8336b4665"));
        clientInfo.setIdMovie(872585);
        clientInfo.setSeats(new String[]{"A1"});
        clientInfo.setTotalPrice(10.00);
        return clientInfo;
    }

    public static ClientInfo createClientToDelete() {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUserId(UUID.fromString("41e822f6-d648-4a1c-acc3-44c8336b4665"));
        clientInfo.setIdMovie(872585);
        clientInfo.setTicketId(99L);
        clientInfo.setSeats(new String[]{"C1"});
        clientInfo.setTotalPrice(10.00);
        return clientInfo;
    }

    public static List<Movie> getMovies() {
        Movie movie1 = new Movie();
        movie1.setId(899789);
        movie1.setTitle("movie1");
        Movie movie2 = new Movie();
        movie2.setId(872585);
        movie2.setTitle("movie2");
        return Arrays.asList(movie1, movie2);
    }

    public static MovieDetail getMovieDetail() {
        MovieDetail movie = new MovieDetail();
        movie.setId(872585);
        movie.setTitle("movie1");
        return movie;
    }
}
