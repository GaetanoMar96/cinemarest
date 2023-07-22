package com.project.cinemarest.service;


import static com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder.eq;

import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder;
import com.project.cinemarest.exception.BadRequestException;
import com.project.cinemarest.exception.SqlConnectionException;
import com.project.cinemarest.factory.PriceCalculatorFactory;
import com.project.cinemarest.mapper.TransactionMapper;
import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.model.Transaction;
import com.project.cinemarest.repository.QueryJdbcConnector;
import com.project.cinemarest.factory.TicketPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class TicketService {

    private static final String TICKET_SEQUENCE = "cinema.seq_cinema_ticket";

    private final TransactionsService transactionsService;
    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;

    private final TransactionMapper transactionMapper;

    public ResponseEntity<Void> postMovieTicket(ClientInfo clientInfo) {
        try {
            //Retrieve id of the ticket to be inserted
            Long ticketId = jdbcConnector.nextVal(TICKET_SEQUENCE);
            //Insert ticket inside table
            insertMovieTicket(ticketId, clientInfo);
            //call transaction controller
            callTransactionService(ticketId, clientInfo);
            //Update cinema hall deleting the chosen seat
            updateCinemaHall(ticketId, clientInfo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadRequestException badRequestException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SqlConnectionException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void insertMovieTicket(Long ticketId, ClientInfo clientInfo) {
        if (clientInfo.getIdMovie() == null) {
            throw new BadRequestException();
        }
        try {
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getInsertMovieTicket());
            queryBuilder.params(ticketId, clientInfo.getIdMovie(), calculateTicketPrice(clientInfo));
            jdbcConnector.insert(queryBuilder.build());
        } catch (SqlConnectionException exception) {
            throw new SqlConnectionException("Error while inserting ticket");
        }
    }

    private void callTransactionService(Long ticketId, ClientInfo clientInfo) {
        Transaction transaction = transactionMapper.mapTransaction(ticketId, clientInfo);
        transactionsService.insertTransaction(transaction);
    }

    private void updateCinemaHall(Long ticketId, ClientInfo clientInfo) {
        if (clientInfo.getSeat() == null) {
            throw new BadRequestException();
        }
        try {
            String seat = clientInfo.getSeat().toString();
            String query = StringUtils.replace(jdbcQueryMovie.getUpdateCinemaHall(), "{SEAT}", seat);
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(query);
            queryBuilder.and(eq("ID_MOVIE", clientInfo.getIdMovie()));
            jdbcConnector.update(queryBuilder.build());
        } catch (SqlConnectionException exception) {
            //deleting both transaction and ticket
            String queryDeleteTransaction = StringUtils.replace(jdbcQueryMovie.getDeleteTransaction(), "{TICKET_ID}", ticketId.toString());
            String queryDeleteMovieTicket = StringUtils.replace(jdbcQueryMovie.getDeleteMovieTicket(), "{TICKET_ID}", ticketId.toString());
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(queryDeleteTransaction.concat(queryDeleteMovieTicket));
            jdbcConnector.update(queryBuilder.build());
            throw new SqlConnectionException("Error while updating cinema hall");
        }
    }

    private double calculateTicketPrice(ClientInfo clientInfo) {
        Integer age = clientInfo.getAge();
        Boolean isStudent = clientInfo.getIsStudent();
        TicketPriceCalculator calculator = PriceCalculatorFactory.createPriceCalculator(age, isStudent);
        return calculator.calculateTicketPrice(age);
    }
}
