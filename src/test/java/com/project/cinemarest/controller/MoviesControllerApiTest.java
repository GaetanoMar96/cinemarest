package com.project.cinemarest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.cinemarest.CoreTestSpringConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.TestcontainersConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {CoreTestSpringConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@WithMockUser(roles = "USER")
public class MoviesControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    //@Container
    public static MongoDBContainer container= new MongoDBContainer("mongo:7.0.1")
        .withExposedPorts(27017)
        .withCopyFileToContainer(MountableFile.forClasspathResource("./scripts/init-schema.js"), "/docker-entrypoint-initdb.d/init-script.js");

    @BeforeAll
    public static void setup() {
        container.withReuse(true);
        container.start();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", container::getHost);
        registry.add("spring.data.mongodb.port", container::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "test_container");
        registry.add("spring.data.mongodb.password", () -> "test_container");
        registry.add("spring.data.mongodb.database", () -> "cinema");
    }

    @Test
    void getAllMovies_expectedStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/movies")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.movies").value(Matchers.hasSize(6)));
    }

    @Test
    void getMovieInfo_expectedStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/movies/{movie}/info", "Interstellar")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.movies.[0].title").value("Interstellar"))
            .andExpect(jsonPath("$.movies.[0].year").value("2014"))
            .andExpect(jsonPath("$.movies.[0].rated").value("PG-13"))
            .andExpect(jsonPath("$.movies.[0].released").value("07 Nov 2014"));
    }

    @Test
    void getMovieInfos_expectedStatus404() throws Exception {
        mockMvc.perform(get("/api/v1/cinema/movies/{movie}/info", "Magnolia")
                            .headers(new HttpHeaders())
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @AfterAll
    public static void tearDown() {
        container.stop();
    }
}
