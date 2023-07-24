package com.project.cinemarest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = "com.project.cinemarest")
@EnableTransactionManagement
@RequiredArgsConstructor
public class CoreTestSpringConfiguration {


}

