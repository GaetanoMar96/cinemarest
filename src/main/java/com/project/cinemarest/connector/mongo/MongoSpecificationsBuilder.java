package com.project.cinemarest.connector.mongo;

import com.project.cinemarest.connector.mongo.MongoSpecification.Operation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class MongoSpecificationsBuilder<T> {

    private final List<SearchCriteria> params;

    public MongoSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public void with(String key, Operation operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
    }

    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new MongoSpecification<>(param));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}
