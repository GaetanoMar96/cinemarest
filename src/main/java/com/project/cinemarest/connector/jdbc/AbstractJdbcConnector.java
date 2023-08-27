package com.project.cinemarest.connector.jdbc;

import com.project.cinemarest.connector.jdbc.utils.JdbcDataSourceProperties;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.exception.SqlConnectionException;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractJdbcConnector<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractJdbcConnector.class);

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcDataSourceProperties jdbcDataSourceProperties;

    @PostConstruct
    private void init() {
        DataSource dataSource = this.jdbcDataSourceProperties.getDataSourceProperties().initializeDataSourceBuilder().build();
        if (dataSource == null) {
            throw new ExceptionInInitializerError();
        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public <R> Optional<R> findOne(JdbcQuery jdbcQuery, Class<R> clazz) {
        RowMapper<R> rowMapper = new BeanPropertyRowMapper<>(clazz);
        List<R> results = this.jdbcTemplate.query(jdbcQuery.getQuery(), rowMapper, jdbcQuery.getArrayParameters());
        return Optional.ofNullable(CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    public <R> List<R> findMany(JdbcQuery jdbcQuery, Class<R> clazz) {
        RowMapper<R> rowMapper = new BeanPropertyRowMapper<>(clazz);
        return this.jdbcTemplate.query(jdbcQuery.getQuery(), rowMapper, jdbcQuery.getArrayParameters());
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

    private String getCountQuery(String tableName) {
        return StringUtils.replace("SELECT COUNT(*) FROM {TABLE_NAME}", "{TABLE_NAME}", tableName);
    }

    public Long getCount(String tableName) {
        return this.executeCount(getCountQuery(tableName));
    }

    public Long nextVal(String sequenceName) {
        return this.executeSequence(nextValSequenceQuery(sequenceName));
    }

    private String nextValSequenceQuery(String sequenceName) {
        return StringUtils.replace("SELECT NEXTVAL('{SEQUENCE}') AS VALUE", "{SEQUENCE}", sequenceName);
    }

    /**
     * Generic method to execute update, delete and insert
     * @param qry query to be executed
     * @param args arguments to pass to the query
     */
    protected void cud(String qry, List<Object> args) {
        try {
            int resultSize = this.jdbcTemplate.update(qry, args.toArray());
            if (resultSize > 0) {
                logger.debug("JDBC Query correctly executed.");
            } else {
                logger.error("Failed Update JDBC Query.");
            }
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }

    protected Long executeSequence(String query) {
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

    protected Long executeCount(String query) {
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

    public T getNumberValue(String query, Class<T> clazz) {
        try {
            return this.jdbcTemplate.queryForObject(query, clazz);
        } catch (DataAccessException exception) {
            throw new SqlConnectionException(exception.getMessage());
        }
    }
}
