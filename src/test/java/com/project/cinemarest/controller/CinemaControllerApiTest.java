package com.project.cinemarest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.cinemarest.BaseIntegrationTest;
import com.project.cinemarest.connector.rest.movies.GetMoviesConnector;
import com.project.cinemarest.entity.Show;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestClientException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.project.cinemarest.utils.TestUtils.getMovies;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@WithMockUser(roles = "USER")
public class CinemaControllerApiTest extends BaseIntegrationTest {

    @MockBean
    private GetMoviesConnector getMoviesConnector;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:9.6.8")
        .withDatabaseName("postgres");

    @BeforeAll
    public static void setup() {
        container.withReuse(true);
        container.start();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @Test
    void getShowsByMovie_expectedStatus200() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/v1/cinema/theater/Inception/shows")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        List<Show> shows = super.getListFromResponse(res.getResponse().getContentAsString(), new TypeReference<List<Show>>() {});
        Assertions.assertEquals(3, shows.size());
    }

    @Test
    void getAllSeatsByMovie_expectedStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/theater/Inception/seats/2023-08-10/16:00")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("availableSeats[0]").value("A1"));
    }

    @Test
    void getShowsByMovie_expectedStatus404() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/v1/cinema/theater/Magnolia/shows")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        System.out.println(res.getResponse().getContentAsString());
    }

    @Test
    void getAllSeatsByMovie_expectedStatus404() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/theater/Interstellar/seats/2023-08-10/17:00")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void postMovies_expectedStatus201() throws Exception {
        when(getMoviesConnector.getMovies()).thenReturn(getMovies());
        mockMvc.perform(post("/api/v1/cinema/theater/stream_movies")
                        .headers(new HttpHeaders())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void postMovies_expectedStatus500() throws Exception {
        when(getMoviesConnector.getMovies()).thenThrow(RestClientException.class);
        mockMvc.perform(post("/api/v1/cinema/theater/stream_movies")
                        .headers(new HttpHeaders())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @AfterAll
    public static void tearDown() {
        container.stop();
    }
}
