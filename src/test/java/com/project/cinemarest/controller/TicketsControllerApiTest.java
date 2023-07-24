package com.project.cinemarest.controller;

import static com.project.cinemarest.utils.TestUtils.createClient;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.cinemarest.BaseIntegrationTest;
import com.project.cinemarest.model.ClientInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TicketsControllerApiTest extends BaseIntegrationTest {

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
    void postMovieTicket_expectedStatus201() throws Exception {
        ClientInfo clientInfo = createClient(21);
        mockMvc.perform(post("/api/v1/cinema/tickets/ticket")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clientInfo)))
            .andExpect(status().isCreated());
    }

    @Test
    void postMovieTicket_expectedStatus201_LowPrice() throws Exception {
        ClientInfo clientInfo = createClient(16);
        mockMvc.perform(post("/api/v1/cinema/tickets/ticket")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clientInfo)))
            .andExpect(status().isCreated());
    }

    @Test
    void postMovieTicket_missingIdMovie_expectedStatus400() throws Exception {
        ClientInfo clientInfo = createClient(21);
        clientInfo.setIdMovie(null);
        mockMvc.perform(post("/api/v1/cinema/tickets/ticket")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clientInfo)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void postMovieTicket_missingIdUser_expectedStatus400() throws Exception {
        ClientInfo clientInfo = createClient(18);
        clientInfo.setUserId(null);
        mockMvc.perform(post("/api/v1/cinema/tickets/ticket")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clientInfo)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void postMovieTicket_missingSeat_expectedStatus400() throws Exception {
        ClientInfo clientInfo = createClient(30);
        clientInfo.setSeat(null);
        mockMvc.perform(post("/api/v1/cinema/tickets/ticket")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clientInfo)))
            .andExpect(status().isBadRequest());
    }

    @AfterAll
    public static void tearDown() {
        container.stop();
    }
}
