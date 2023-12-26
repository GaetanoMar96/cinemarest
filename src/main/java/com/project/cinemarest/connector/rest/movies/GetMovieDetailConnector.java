package com.project.cinemarest.connector.rest.movies;

import com.project.cinemarest.connector.rest.AbstractRestConnector;
import com.project.cinemarest.model.MovieDetail;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GetMovieDetailConnector extends AbstractRestConnector<Void, MovieDetail> {

    private static final String ENDPOINT = "https://api.themoviedb.org/3/movie/";

    private static final HttpMethod httpMethod = HttpMethod.GET;

    public MovieDetail getMovie(String movieId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("language", "en-US");
        return getResponse(ENDPOINT.concat(movieId), httpMethod, queryParams, MovieDetail.class);
    }
}
