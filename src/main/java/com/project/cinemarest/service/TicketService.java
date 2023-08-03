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
        try {
            logger.info("Retrieving id of the ticket to be inserted");
            Long ticketId = jdbcConnector.nextVal(TICKET_SEQUENCE);
            logger.info("Inserting ticket inside table");
            insertMovieTicket(ticketId, clientInfo);
            logger.info("Call transaction controller");
            callTransactionService(ticketId, clientInfo);
            logger.info("Updating cinema hall deleting the chosen seat");
            updateCinemaHall(ticketId, clientInfo);
            logger.info("Updating client wallet");
            callUserService(clientInfo);
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
            logger.error("Deleting both transaction and ticket");
            String queryDeleteTransaction = StringUtils.replace(jdbcQueryMovie.getDeleteTransaction(), "{TICKET_ID}", ticketId.toString());
            String queryDeleteMovieTicket = StringUtils.replace(jdbcQueryMovie.getDeleteMovieTicket(), "{TICKET_ID}", ticketId.toString());
            JdbcQueryBuilder queryBuilder = new JdbcQueryBuilder(queryDeleteTransaction.concat(queryDeleteMovieTicket));
            jdbcConnector.delete(queryBuilder.build());
            throw new SqlConnectionException("Error while updating cinema hall");
        }
    }

    private void callUserService(ClientInfo clientInfo) {
        //update user wallet
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
