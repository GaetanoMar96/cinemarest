package com.project.cinemarest.connector.mongo;

import com.project.cinemarest.connector.mongo.MongoSpecification.Operation;
import lombok.Getter;

@Getter
public class SearchCriteria {

    private final String key;
    private final Operation operation;
    private final Object value;

    public SearchCriteria(String key, Operation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
