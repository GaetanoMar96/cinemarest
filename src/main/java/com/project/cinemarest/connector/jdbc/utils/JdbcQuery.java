package com.project.cinemarest.connector.jdbc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcQuery {

    private final StringBuilder queryBuilder;

    private final List<Object> parameters = new ArrayList<>();

    public String getQuery() {
        return queryBuilder.toString();
    }

    public Object[] getArrayParameters() {
        return parameters.toArray();
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Object> params) {
        this.parameters.addAll(params);
    }

    public void setParameters(Object... params) {
        this.parameters.addAll(Arrays.asList(params));
    }

    public JdbcQuery(String query) {
        queryBuilder = new StringBuilder(query);
    }

    /**
     * Method to concatenate filters into where clause
     * @param operator AND | OR
     * @param colName column
     * @param value value
     */
    public void eq(OperatorEnum operator, String colName, Object value) {
        queryBuilder.append(operator);
        queryBuilder.append(colName.concat(" = ?"));
        parameters.add(value);
    }

    public enum OperatorEnum {
        AND(" AND "),
        OR(" OR ");

        private final String operator;

        @Override
        public String toString() {
            return String.valueOf(this.operator);
        }

        OperatorEnum(String operator) {
            this.operator = operator;
        }
    }
}
