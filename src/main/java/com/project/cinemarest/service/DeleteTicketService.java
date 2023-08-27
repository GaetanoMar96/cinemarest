package com.project.cinemarest.service;


import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.OperatorEnum;
import com.project.cinemarest.connector.jpa.repo.TicketRepository;
import com.project.cinemarest.exception.BadRequestException;
import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.connector.jdbc.QueryJdbcConnector;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DeleteTicketService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteTicketService.class);

    private final TransactionsService transactionsService;

    private final UserService userService;

    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;

    private final TicketRepository ticketRepository;

    private Double cost;

    public ResponseEntity<Void> deleteMovieTicket(ClientInfo clientInfo) {
        logger.info("Storing synchronously the ticket cost to be updated at the end");
        retrieveTicketCost(clientInfo.getTicketId());
        logger.info("calling asynchronously all the operations to delete the purchased ticket");
        deleteTicketAsync(clientInfo);
        logger.info("Updating user wallet");
        updateUserWalletSync(clientInfo.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method to execute asynchronously all the deleting and updating operations
     * after the user requested a ticket cancellation
     * @param clientInfo client info
     */
    private void deleteTicketAsync(ClientInfo clientInfo) {
        Long ticketId = clientInfo.getTicketId();

        logger.info("Updating cinema hall adding the chosen seat");
        CompletableFuture<Void> updateCinemaHallFuture = CompletableFuture.runAsync(() -> updateCinemaHallAddingSeat(clientInfo));
        logger.info("Call transaction controller to delete transaction");
        CompletableFuture<Void> deleteTransactionFuture = CompletableFuture.runAsync(() -> transactionsService.deleteTransaction(ticketId));
        logger.info("Deleting the ticket");
        CompletableFuture<Void> deleteMovieTicketFuture = CompletableFuture.runAsync(() -> ticketRepository.deleteTicket(ticketId));

        CompletableFuture.allOf(updateCinemaHallFuture, deleteTransactionFuture, deleteMovieTicketFuture)
            .thenRun(() -> logger.info("Deletion completed"))
            .exceptionally(e -> {
                logger.info("Deletion not completed");
                return null;
            });
    }

    private void updateCinemaHallAddingSeat(ClientInfo clientInfo) {
        if (clientInfo.getSeats() == null) {
            throw new BadRequestException("seat cannot be null or negative");
        }

        String[] seats = clientInfo.getSeats();
        for(String seat : seats) {
            String query = StringUtils.replace(jdbcQueryMovie.getUpdateCinemaHallAddingSeat(), "{SEAT}", seat);
            JdbcQuery jdbcQuery = new JdbcQuery(query);
            jdbcQuery.eq(OperatorEnum.AND, "ID_MOVIE", clientInfo.getIdMovie());
            jdbcConnector.update(jdbcQuery);
        }
    }

    private void retrieveTicketCost(Long ticketId) {
        String query = StringUtils.replace(jdbcQueryMovie.getSelectPriceForMovieTicket(), "{TICKET_ID}", ticketId.toString());
        this.cost = (Double) jdbcConnector.getNumberValue(query, Double.class);
    }

    private void updateUserWalletSync(UUID userId) {
        userService.updateUserWallet(this.cost, userId);
    }
}
