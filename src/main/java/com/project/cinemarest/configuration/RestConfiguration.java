package com.project.cinemarest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestConfiguration {

    @Bean
    public WebClient localApiClient() {
        return WebClient.create("http://localhost:8091/api/v1/cinema");
    }
}
