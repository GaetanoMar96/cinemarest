package com.project.cinemarest.service;


import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.OperatorEnum;
import com.project.cinemarest.connector.jpa.repo.TicketRepository;
import com.project.cinemarest.entity.Ticket;
import com.project.cinemarest.exception.BadRequestException;
import com.project.cinemarest.exception.SqlConnectionException;
import com.project.cinemarest.factory.PriceCalculatorFactory;
import com.project.cinemarest.mapper.TicketMapper;
import com.project.cinemarest.mapper.TransactionMapper;
import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.entity.Transaction;
import com.project.cinemarest.connector.jdbc.QueryJdbcConnector;
import com.project.cinemarest.factory.TicketPriceCalculator;
import javax.persistence.PersistenceException;
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

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

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
        if (clientInfo.getIdMovie() == null) {
            throw new BadRequestException("id movie cannot be null or <= 0");
        }
        try {
            Ticket ticket = ticketMapper.mapTicket(clientInfo);
            ticket.setCost(calculateTicketPrice(clientInfo));
            ticketRepository.saveAndFlush(ticket);
        } catch (PersistenceException exception) {
            throw new SqlConnectionException("Error while inserting ticket");
        }
    }

    private void callTransactionService(ClientInfo clientInfo) {
        Transaction transaction = transactionMapper.mapTransaction(clientInfo);
        transactionsService.insertTransaction(transaction);
    }

    private void updateCinemaHall(ClientInfo clientInfo) {
        if (clientInfo.getSeats() == null) {
            throw new BadRequestException("seat cannot be null or negative");
        }
        try {
            String[] seats = clientInfo.getSeats();
            for(String seat : seats) {
                String query = StringUtils.replace(jdbcQueryMovie.getUpdateCinemaHall(), "{SEAT}", seat);
                JdbcQuery jdbcQuery = new JdbcQuery(query);
                jdbcQuery.eq(OperatorEnum.AND, "ID_MOVIE", clientInfo.getIdMovie());
                jdbcConnector.update(jdbcQuery);
            }
        } catch (SqlConnectionException exception) {
            logger.error("Deleting both transaction and ticket");
            transactionsService.deleteTransaction(clientInfo.getTicketId());
            ticketRepository.deleteTicket(clientInfo.getTicketId());
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
