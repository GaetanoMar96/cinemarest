package com.project.cinemarest.connector.jdbc.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JdbcQueryType {
    FIND("FIND"),
    UPDATE("UPDATE"),
    EXECUTE("EXECUTE");

    private final String type;

    @Override
    public String toString() {
        return this.type;
    }
}
