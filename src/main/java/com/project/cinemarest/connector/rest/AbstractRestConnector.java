package com.project.cinemarest.connector.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class AbstractRestConnector<I, O>{

    protected O getResponse(String endpoint, HttpMethod httpMethod, MultiValueMap<String, String> queryParams, Class<O> clazz) {
        HttpEntity<?> entity = setHeaders(); //set headers
        String uri = setUri(endpoint, queryParams);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<O> response = restTemplate.exchange(
            uri,
            httpMethod,
            entity,
            clazz);

        return response.getBody();
    }

    private String setUri(String endpoint, MultiValueMap<String, String> queryParams) {
        // Build the URI with query parameters
        String apiKey = System.getenv("API_KEY");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint)
            .queryParam("api_key", apiKey)
            .queryParams(queryParams);
        return builder.toUriString();
    }

    private HttpEntity<?> setHeaders() {
        // Create headers with authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5YjEyZjc5NmFlYWQ3NGU0NWYyYjQyYzBiODk4N2MxNCIsInN1YiI6IjY1MDZmM2QwMTA5ZGVjMDE0ZjQxNDMwYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.R6U8WsBN8Sk2YGyj5qN8eNFd0PCmCaQteGz5t7-ehc0");

        // Create an HTTP entity with headers
        return new HttpEntity<>(headers);
    }

}
