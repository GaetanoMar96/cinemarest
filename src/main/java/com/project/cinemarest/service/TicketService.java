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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private static final String TICKET_SEQUENCE = "cinema.seq_cinema_ticket";

    private final TransactionsService transactionsService;

    private final UserService userService;
    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;

    private final TransactionMapper transactionMapper;

    public ResponseEntity<Void> postMovieTicket(ClientInfo clientInfo) {
        logger.info("Retrieving id of the ticket to be inserted");
        Long ticketId = jdbcConnector.nextVal(TICKET_SEQUENCE);
        clientInfo.setTicketId(ticketId);
        logger.info("Inserting ticket inside table");
        insertMovieTicket(clientInfo);
        logger.info("Call transaction controller");
        callTransactionService(clientInfo);
        logger.info("Updating cinema hall deleting the chosen seat");
        updateCinemaHall(clientInfo);
        logger.info("Updating client wallet");
        callUserService(clientInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void insertMovieTicket(ClientInfo clientInfo) {
        if (clientInfo.getIdMovie() == null || clientInfo.getIdMovie() <= 0) {
            throw new BadRequestException("id movie cannot be null or <= 0");
        }
        try {
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(jdbcQueryMovie.getInsertMovieTicket());
            queryBuilder.params(clientInfo.getTicketId(), clientInfo.getIdMovie(), calculateTicketPrice(clientInfo));
            jdbcConnector.insert(queryBuilder.build());
        } catch (SqlConnectionException exception) {
            throw new SqlConnectionException("Error while inserting ticket");
        }
    }

    private void callTransactionService(ClientInfo clientInfo) {
        Transaction transaction = transactionMapper.mapTransaction(clientInfo);
        transactionsService.insertTransaction(transaction);
    }

    private void updateCinemaHall(ClientInfo clientInfo) {
        if (clientInfo.getSeat() == null || clientInfo.getSeat() <= 0) {
            throw new BadRequestException("seat cannot be null or negative");
        }
        try {
            String seat = clientInfo.getSeat().toString();
            String query = StringUtils.replace(jdbcQueryMovie.getUpdateCinemaHall(), "{SEAT}", seat);
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(query);
            queryBuilder.and(eq("ID_MOVIE", clientInfo.getIdMovie()));
            jdbcConnector.update(queryBuilder.build());
        } catch (SqlConnectionException exception) {
            logger.error("Deleting both transaction and ticket");
            String queryDeleteTransaction = StringUtils.replace(jdbcQueryMovie.getDeleteTransaction(), "{TICKET_ID}", clientInfo.getTicketId().toString());
            String queryDeleteMovieTicket = StringUtils.replace(jdbcQueryMovie.getDeleteMovieTicket(), "{TICKET_ID}", clientInfo.getTicketId().toString());
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(queryDeleteTransaction.concat(queryDeleteMovieTicket));
            jdbcConnector.delete(queryBuilder.build());
            throw new SqlConnectionException("Error while updating cinema hall");
        }
    }

    private void callUserService(ClientInfo clientInfo) {
        logger.info("updating user wallet");
        userService.updateUserWallet(clientInfo.getWallet(), clientInfo.getUserId());
    }

    private double calculateTicketPrice(ClientInfo clientInfo) {
        Integer age = clientInfo.getAge();
        Boolean isStudent = clientInfo.getIsStudent();
        TicketPriceCalculator calculator = PriceCalculatorFactory.createPriceCalculator(age, isStudent);
        double price = calculator.calculateTicketPrice(age);
        clientInfo.setWallet(clientInfo.getWallet() - price);
        return price;
    }
}
