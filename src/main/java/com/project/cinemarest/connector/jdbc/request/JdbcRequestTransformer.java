package com.project.cinemarest.connector.jdbc.request;

public interface JdbcRequestTransformer<I, D> {

    JdbcRequest<D> transformInput(I om, Object... args);
}
