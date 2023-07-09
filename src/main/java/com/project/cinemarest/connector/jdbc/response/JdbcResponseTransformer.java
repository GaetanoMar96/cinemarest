package com.project.cinemarest.connector.jdbc.response;

public interface JdbcResponseTransformer<R, O> {

    O transformOutput(JdbcResponse<R> jdbcResponse);
}
