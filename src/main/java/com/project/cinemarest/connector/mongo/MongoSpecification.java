package com.project.cinemarest.connector.mongo;

import java.util.EnumMap;
import java.util.function.BiFunction;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.util.Map;

public class MongoSpecification<T> implements Specification<T> {

    private static SearchCriteria criteria = null;

    public MongoSpecification(SearchCriteria criteria) {
        MongoSpecification.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<?> keyPath = root.get(criteria.getKey());
        BiFunction<CriteriaBuilder, Path<?>, Predicate> operationFunction = getOperationFunction(criteria.getOperation());

        if (operationFunction != null) {
            return operationFunction.apply(builder, keyPath);
        }

        return null;
    }

    private static BiFunction<CriteriaBuilder, Path<?>, Predicate> getOperationFunction(Operation operation) {
        Map<Operation, BiFunction<CriteriaBuilder, Path<?>, Predicate>> operationMap = new EnumMap<>(Operation.class);
        operationMap.put(Operation.EQ, (builder, path) -> builder.equal(path, criteria.getValue()));
        operationMap.put(Operation.GTE, (builder, path) -> builder.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) criteria.getValue()));
        operationMap.put(Operation.LTE, (builder, path) -> builder.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) criteria.getValue()));
        operationMap.put(Operation.RGX, (builder, path) -> builder.like(path.as(String.class), "%" + criteria.getValue() + "%"));
        return operationMap.get(operation);
    }

    public enum Operation {
        EQ(":"),
        GTE(">="),
        LTE("<="),
        RGX("regex");

        private final String value;

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }

        Operation(String value) {
            this.value = value;
        }
    }
}
