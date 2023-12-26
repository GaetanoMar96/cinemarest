package com.project.cinemarest.controller;

import static com.project.cinemarest.utils.TestUtils.getMovieDetail;
import static com.project.cinemarest.utils.TestUtils.getMovies;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.cinemarest.CoreTestSpringConfiguration;
import com.project.cinemarest.connector.rest.movies.GetMovieDetailConnector;
import com.project.cinemarest.connector.rest.movies.GetMoviesConnector;
import com.project.cinemarest.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ContextConfiguration(classes = {CoreTestSpringConfiguration.class})
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MoviesControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetMoviesConnector getMoviesConnector;

    @MockBean
    private GetMovieDetailConnector getMovieDetailConnector;

    @BeforeEach
    void setUp() {
        when(getMoviesConnector.getMovies()).thenReturn(getMovies());
        when(getMoviesConnector.getUpcomingMovies()).thenReturn(getMovies());
        when(getMovieDetailConnector.getMovie("872585")).thenReturn(getMovieDetail());
    }

    @Test
    void getAllMovies_expectedStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/movies/db/now_playing")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void getMovieDetail_expectedStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/movies/db/movie/{movie_id}", "872585")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("movie1"))
            .andExpect(jsonPath("$.id").value(872585));
    }

    @Test
    void getMovieDetail_expectedStatus404() throws Exception {
        when(getMovieDetailConnector.getMovie("872586")).thenThrow(DataNotFoundException.class);

        mockMvc.perform(get("/api/v1/cinema/movies/db/movie/{movie_id}", "872586")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void getUpcomingMovies_expectedStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/movies/db/upcoming")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
