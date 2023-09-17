package com.project.cinemarest.connector.rest.movies;

import com.project.cinemarest.connector.rest.AbstractRestConnector;
import com.project.cinemarest.model.MovieResponse;
import com.project.cinemarest.model.MovieResponse.Movie;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GetMoviesConnector extends AbstractRestConnector<Void, MovieResponse> {

    private static final String ENDPOINT = "https://api.themoviedb.org/3/movie/now_playing";

    private static final HttpMethod httpMethod = HttpMethod.GET;

    public List<Movie> getMovies() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("language", "en-US");
        queryParams.add("page", "1");
        queryParams.add("region", "IT");
        return getResponse(ENDPOINT, httpMethod, queryParams, MovieResponse.class).getResults();
    }
}
