package com.project.cinemarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ContextConfiguration(classes = {CoreTestSpringConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = {"/scripts/create.sql", "/scripts/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    protected <T> T getObjectFromResponse(String response, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(response, clazz);
    }

    protected <T> List<T> getListFromResponse(String response, TypeReference<List<T>> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(response, typeReference);
    }
}
