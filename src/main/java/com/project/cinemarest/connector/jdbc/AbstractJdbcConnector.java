package com.project.cinemarest.connector.jdbc;

import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.connector.jdbc.utils.JdbcQueryType;
import com.project.cinemarest.connector.jdbc.request.JdbcRequest;
import com.project.cinemarest.connector.jdbc.request.JdbcRequestTransformer;
import com.project.cinemarest.connector.jdbc.response.JdbcResponse;
import com.project.cinemarest.connector.jdbc.response.JdbcResponseTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

public abstract class AbstractJdbcConnector extends JdbcConnector<String, List<Object>, Void, Object> implements JdbcRequestTransformer<String, Void>, JdbcResponseTransformer<Object, List<Object>> {

    @Override
    public JdbcRequest<Void> transformInput(String om, Object... args) {
        JdbcRequest<Void> jdbcConnectorRequest = new JdbcRequest<>();
        JdbcQueryType queryType = (JdbcQueryType)args[0];
        jdbcConnectorRequest.setQuery(om);
        jdbcConnectorRequest.setType(queryType);
        if (JdbcQueryType.EXECUTE != queryType) {
            jdbcConnectorRequest.setRowMapper(new BeanPropertyRowMapper<>((Class)args[1]));
            if (args.length > 2) {
                jdbcConnectorRequest.setParams(Arrays.copyOfRange(args, 2, args.length));
            }
        }
        return jdbcConnectorRequest;
    }

    public <R> Optional<R> findOne(String qry, Class<R> rowMapper, Object... args) {
        List<R> results = this.convertListObjectToGeneric(rowMapper, super.call(qry, this, this, this.createArgsArray(JdbcQueryType.FIND, rowMapper, Arrays.asList(args))));
        return Optional.ofNullable(org.springframework.util.CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    public <R> List<R> findMany(String qry, Class<R> rowMapper, Object... args) {
        return this.convertListObjectToGeneric(rowMapper, super.call(qry, this, this, this.createArgsArray(JdbcQueryType.FIND, rowMapper, Arrays.asList(args))));
    }

    public <R> Optional<R> findOne(JdbcQuery jdbcQuery, Class<R> rowMapper) {
        List<R> results = this.convertListObjectToGeneric(rowMapper, super.call(jdbcQuery.getQuery(), this, this, this.createArgsArray(JdbcQueryType.FIND, rowMapper, jdbcQuery.getParameters())));
        return Optional.ofNullable(org.springframework.util.CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    public <R> List<R> findMany(JdbcQuery jdbcQuery, Class<R> rowMapper) {
        return this.convertListObjectToGeneric(rowMapper, super.call(jdbcQuery.getQuery(), this, this, this.createArgsArray(JdbcQueryType.FIND, rowMapper, jdbcQuery.getParameters())));
    }

    public Long count(String tableName) {
        return super.caseQueryTypeCOUNT(getCountQuery(tableName));
    }

    private String getCountQuery(String tableName) {
        return StringUtils.replace("SELECT COUNT(*) FROM {TABLE_NAME}", "{TABLE_NAME}", tableName);
    }

    public void insert(String qry, Object... args) {
        this.cud(qry, Arrays.asList(args));
    }

    public void update(String qry, Object... args) {
        this.cud(qry, Arrays.asList(args));
    }

    public void delete(String qry, Object... args) {
        this.cud(qry, Arrays.asList(args));
    }

    public void insert(JdbcQuery jdbcQuery) {
        this.cud(jdbcQuery.getQuery(), jdbcQuery.getParameters());
    }

    public void update(JdbcQuery jdbcQuery) {
        this.cud(jdbcQuery.getQuery(), jdbcQuery.getParameters());
    }

    public void delete(JdbcQuery jdbcQuery) {
        this.cud(jdbcQuery.getQuery(), jdbcQuery.getParameters());
    }

    private void cud(String qry, List<Object> args) {
        this.call(qry, this, this, this.createArgsArray(JdbcQueryType.UPDATE, Object.class, args));
    }

    private <R> Object[] createArgsArray(JdbcQueryType queryType, Class<R> rowMapper, List<Object> args) {
        List<Object> argsList = new ArrayList<>(3);
        argsList.add(queryType);
        argsList.add(rowMapper);
        argsList.addAll(args);
        return argsList.toArray();
    }

    private <R> List<R> convertListObjectToGeneric(Class<R> returnType, List<Object> toConvert) {
        Stream<Object> variable = toConvert.stream();
        variable = variable.filter(returnType::isInstance);
        return variable.map(returnType::cast).collect(Collectors.toList());
    }

    public Long nextVal(String sequenceName) {
        return super.caseQueryTypeSEQUENCE(nextValSequenceQuery(sequenceName));
    }

    private String nextValSequenceQuery(String sequenceName) {
        return StringUtils.replace("SELECT NEXTVAL('{SEQUENCE}') AS VALUE", "{SEQUENCE}", sequenceName);
    }

    @Override
    public List<Object> transformOutput(JdbcResponse<Object> jdbcResponse) {
        if (jdbcResponse.getResult() != null) {
            return jdbcResponse.getResult();
        } else {
            return Collections.emptyList();
        }
    }
}
