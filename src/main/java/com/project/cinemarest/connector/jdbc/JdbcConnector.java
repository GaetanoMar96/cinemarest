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
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

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
        if (dataSource == null)
            throw new ExceptionInInitializerError();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        //this.jdbcTemplate.setFetchSize();
    }

    protected JdbcResponse execute(JdbcRequest<D> request) {
        JdbcResponse<R> response = new JdbcResponse<>();
        JdbcQueryType type = request.getType();
        String query = request.getQuery();
        Object[] params = request.getParams();
        RowMapper rowMapper = request.getRowMapper();
        ResultSetExtractor resultSetExtractor = request.getResultSetExtractor();
        boolean isRowMapper = false;
        if (request.getType().equals(JdbcQueryType.FIND)) {
            if (request.getRowMapper() != null) {
                isRowMapper = true;
            } else if (request.getResultSetExtractor() == null) {
                throw new IllegalStateException("Either RowMapper or ResultSetExtractor must be set");
            }
        }

        switch (type) {
            case FIND:
                this.caseQueryTypeFIND(isRowMapper, query, rowMapper, params, response, resultSetExtractor, type);
                break;
            case UPDATE:
                this.caseQueryTypeUPDATE(type, query, params, response);
                break;
            case EXECUTE:
                this.caseQueryTypeEXECUTE(request, response, type);
                break;
            default:
                logger.error("Invalid JDBC query type not defined.");
        }
        return response;
    }

    private void caseQueryTypeFIND(boolean isRowMapper, String query, RowMapper rowMapper, Object[] params, JdbcResponse<R> response, ResultSetExtractor resultSetExtractor, JdbcQueryType type) {
        try {
            if (isRowMapper) {
                List<R> resultList = this.jdbcTemplate.query(query, rowMapper, params);
                this.resultSize = CollectionUtils.size(resultList);
                response.setResult(resultList);
            } else {
                response.setResultSetExtr(this.jdbcTemplate.query(query, params, resultSetExtractor));
            }
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    private void caseQueryTypeUPDATE(JdbcQueryType type, String query, Object[] params, JdbcResponse<R> response) {
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

    private void caseQueryTypeEXECUTE(JdbcRequest<D> request, JdbcResponse<R> response, JdbcQueryType type) {
        try {
            Assert.notNull(request.getCallableStatementCreator(), "CallableStatementCreator must not be null");
            Assert.notNull(request.getStoredProcedureParameters(), "Callback object must not be null");
            response.setStoredProcedureResult(this.jdbcTemplate.call(request.getCallableStatementCreator(), request.getStoredProcedureParameters()));
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    public final O call(I input, JdbcRequestTransformer<I, D> requestTransformer, JdbcResponseTransformer<R, O> responseTransformer, Object... args) {
        JdbcRequest<D> jdbcRequest = requestTransformer.transformInput(input, args);
        JdbcResponse<R> jdbcResponse = (JdbcResponse)this.execute(jdbcRequest);
        return responseTransformer.transformOutput(jdbcResponse);
    }
}
