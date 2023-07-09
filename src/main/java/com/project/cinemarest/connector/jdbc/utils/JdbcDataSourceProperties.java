package com.project.cinemarest.connector.jdbc.utils;

import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JdbcDataSourceProperties {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    private String uri;
    private String user;
    private String password;

    @PostConstruct
    void initJdbcDataSourceProperties() {
        this.uri = dataSourceProperties.getUrl();
        this.user = dataSourceProperties.getUsername();
        this.password = dataSourceProperties.getPassword();
    }

    public JdbcDataSourceProperties() {}
}
