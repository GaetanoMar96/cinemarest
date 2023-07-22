package com.project.cinemarest.connector.jdbc;

import com.project.cinemarest.connector.jdbc.utils.JdbcDataSourceProperties;
import com.project.cinemarest.connector.jdbc.utils.JdbcQueryType;
import com.project.cinemarest.connector.jdbc.request.JdbcRequest;
import com.project.cinemarest.connector.jdbc.request.JdbcRequestTransformer;
import com.project.cinemarest.connector.jdbc.response.JdbcResponse;
import com.project.cinemarest.connector.jdbc.response.JdbcResponseTransformer;
import com.project.cinemarest.exception.SqlConnectionException;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public abstract class JdbcConnector<I, O, D, R> {

    private static final Logger logger = LoggerFactory.getLogger(JdbcConnector.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcDataSourceProperties jdbcDataSourceProperties;

    private Integer resultSize = null;

    protected JdbcConnector() {
    }

    @PostConstruct
    private void init() {
        DataSource dataSource = this.jdbcDataSourceProperties.getDataSourceProperties().initializeDataSourceBuilder().build();
        if (dataSource == null) {
            throw new ExceptionInInitializerError();
        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected JdbcResponse<R> execute(JdbcRequest<D> request) {
        JdbcResponse<R> response = new JdbcResponse<>();
        if (request.getType().equals(JdbcQueryType.FIND) && request.getRowMapper() == null) {
            throw new IllegalStateException("RowMapper must be set");
        }
        switchQuery(request, response);
        return response;
    }

    private void switchQuery(JdbcRequest<D> request, JdbcResponse<R> response) {
        JdbcQueryType type = request.getType();
        String query = request.getQuery();
        Object[] params = request.getParams();
        RowMapper rowMapper = request.getRowMapper();
        switch (type) {
            case FIND:
                this.caseQueryTypeFIND(query, rowMapper, params, response);
                break;
            case UPDATE:
                this.caseQueryTypeUPDATE(query, params, response);
                break;
            default:
                logger.error("Invalid JDBC query type not defined.");
        }
    }

    private void caseQueryTypeFIND(String query, RowMapper rowMapper, Object[] params, JdbcResponse<R> response) {
        try {
            List<R> resultList = this.jdbcTemplate.query(query, rowMapper, params);
            this.resultSize = CollectionUtils.size(resultList);
            response.setResult(resultList);
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    private void caseQueryTypeUPDATE(String query, Object[] params, JdbcResponse<R> response) {
        try {
            this.resultSize = this.jdbcTemplate.update(query, params);
            if (this.resultSize > 0) {
                response.setResult(Collections.emptyList());
            } else {
                logger.error("Failed Update JDBC Query.");
            }
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    protected void caseQueryForObject(String query, RowMapper<R> rowMapper) {
        try {
            this.jdbcTemplate.queryForObject(query, rowMapper);
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    protected Long caseQueryTypeSEQUENCE(String query) {
        try {
            Long nextVal = this.jdbcTemplate.queryForObject(query, Long.class);
            if (nextVal == null) {
                throw new SqlConnectionException("Failed retrieving sequence from JDBC Query.");
            }
            return nextVal - 1;
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    protected Long caseQueryTypeCOUNT(String query) {
        try {
            Long count = this.jdbcTemplate.queryForObject(query, Long.class);
            if (count == null) {
                throw new SqlConnectionException("Failed retrieving sequence from JDBC Query.");
            }
            return count;
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    public final O call(I input, JdbcRequestTransformer<I, D> requestTransformer, JdbcResponseTransformer<R, O> responseTransformer, Object... args) {
        JdbcRequest<D> jdbcRequest = requestTransformer.transformInput(input, args);
        JdbcResponse<R> jdbcResponse = this.execute(jdbcRequest);
        return responseTransformer.transformOutput(jdbcResponse);
    }
}
