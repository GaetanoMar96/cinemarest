package com.project.cinemarest.connector.jdbc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JdbcQuery {

    private static final StringBuilder builder = new StringBuilder();

    private String query;
    private List<Object> parameters;

    public String getQuery() {
        return this.query;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public JdbcQuery(String query, List<Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public static class JdbcQueryBuilder {
        private StringBuilder queryBuilder;
        private List<Object> parameters = new ArrayList<>();

        public static Condition eq(String colName, Object value) {
            return new Condition(colName.concat(" = ?"), Collections.singletonList(value));
        }

        public static Condition neq(String colName, Object value) {
            return new Condition(colName.concat(" <> ?"), Collections.singletonList(value));
        }

        public static Condition gt(String colName, Object value) {
            return new Condition(colName.concat(" > ?"), Collections.singletonList(value));
        }

        public static Condition gte(String colName, Object value) {
            return new Condition(colName.concat(" >= ?"), Collections.singletonList(value));
        }

        public static Condition lt(String colName, Object value) {
            return new Condition(colName.concat(" < ?"), Collections.singletonList(value));
        }

        public static Condition lte(String colName, Object value) {
            return new Condition(colName.concat(" <= ?"), Collections.singletonList(value));
        }

        public static Condition in(String colName, List<Object> values) {
            return inNotInHandler("IN", colName, values);
        }

        public static Condition notIn(String colName, List<Object> values) {
            return inNotInHandler("NOT IN", colName, values);
        }

        private static Condition inNotInHandler(String keyword, String colName, List<Object> values) {
            //building IN or NOT IN sql operation
            builder.append(colName).append(" ").append(keyword).append(" (");

            for(int i = 0; i < values.size(); ++i) {
                builder.append("?");
                if (values.size() - 1 != i) {
                    builder.append(",");
                }
            }

            builder.append(")");
            Condition condition = new Condition(builder.toString(), values);
            builder.setLength(0);
            return condition;
        }

        public JdbcQuery build() {
            return new JdbcQuery(this.queryBuilder.toString(), this.parameters);
        }

        public JdbcQueryBuilder(String query) {
            this.queryBuilder = new StringBuilder(query);
        }

        public JdbcQueryBuilder and(Condition... conditions) {
            return this.and(Arrays.asList(conditions));
        }

        public JdbcQueryBuilder or(Condition... conditions) {
            return this.or(Arrays.asList(conditions));
        }

        public JdbcQueryBuilder and(List<Condition> conditions) {
            this.handleConditions(JdbcQuery.JdbcQueryBuilder.CriterionEnum.AND, conditions);
            return this;
        }

        public JdbcQueryBuilder or(List<Condition> conditions) {
            this.handleConditions(JdbcQuery.JdbcQueryBuilder.CriterionEnum.OR, conditions);
            return this;
        }

        private void handleConditions(CriterionEnum criterionEnum, List<Condition> conditions) {
            Iterator<Condition> var3 = conditions.iterator();

            while(var3.hasNext()) {
                Condition cond = var3.next();
                this.queryBuilder.append(criterionEnum).append(cond.queryPart);
                this.parameters.addAll(cond.parameters);
            }

        }

        public JdbcQueryBuilder params(Object... params) {
            this.parameters.addAll(Arrays.asList(params));
            return this;
        }

        public JdbcQueryBuilder orderBy(String orderBy) {
            if (!orderBy.contains("ORDER BY")) {
                this.queryBuilder.append(" ORDER BY ");
            }

            this.queryBuilder.append(orderBy);
            return this;
        }

        public JdbcQueryBuilder limit(int limit) {
            this.queryBuilder.append(" LIMIT ").append(limit);
            return this;
        }

        public JdbcQueryBuilder offset(int offset) {
            this.queryBuilder.append(" OFFSET ").append(offset);
            return this;
        }

        public static class Condition {
            private String queryPart;
            private List<Object> parameters;

            public String getQueryPart() {
                return this.queryPart;
            }

            public List<Object> getParameters() {
                return this.parameters;
            }

            public void setQueryPart(String queryPart) {
                this.queryPart = queryPart;
            }

            public void setParameters(List<Object> parameters) {
                this.parameters = parameters;
            }

            protected boolean canEqual(Object other) {
                return other instanceof Condition;
            }

            public String toString() {
                return "JdbcQuery.JdbcQueryBuilder.Condition(queryPart=" + this.getQueryPart() + ", parameters=" + this.getParameters() + ")";
            }

            public Condition(String queryPart, List<Object> parameters) {
                this.queryPart = queryPart;
                this.parameters = parameters;
            }
        }

        private enum CriterionEnum {
            AND(" AND "),
            OR(" OR ");

            private final String value;

            @Override
            public String toString() {
                return String.valueOf(this.value);
            }

            CriterionEnum(String value) {
                this.value = value;
            }
        }
    }
}
