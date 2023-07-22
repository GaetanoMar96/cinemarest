package com.project.cinemarest.service;

import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder;
import com.project.cinemarest.exception.SqlConnectionException;
import com.project.cinemarest.model.Transaction;
import com.project.cinemarest.repository.QueryJdbcConnector;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;

    public void insertTransaction(Transaction transaction) {
        try {
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getInsertTransaction());
            queryBuilder.params(UUID.randomUUID(), transaction.getTicketId(), transaction.getIdMovie(), transaction.getUserId());
            jdbcConnector.insert(queryBuilder.build());
        } catch (Exception exception) {
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getDeleteMovieTicket());
            jdbcConnector.update(queryBuilder.build());
            throw new SqlConnectionException("Error while inserting transaction");
        }
    }
}
