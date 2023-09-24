package com.project.cinemarest.service;


import com.project.cinemarest.connector.jdbc.query.JdbcQueryMovie;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.OperatorEnum;
import com.project.cinemarest.connector.jpa.repo.TicketRepository;
import com.project.cinemarest.exception.BadRequestException;
import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.connector.jdbc.QueryJdbcConnector;
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
    private final QueryJdbcConnector jdbcConnector;
    private final JdbcQueryMovie jdbcQueryMovie;
    private final TicketRepository ticketRepository;

    public ResponseEntity<Void> deleteMovieTicket(ClientInfo clientInfo) {
        updateSeatsAsync(clientInfo);
        deleteTicketsSync(clientInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method to execute asynchronously all the updating operations
     * after the user requested a ticket cancellation
     * @param clientInfo client info
     */
    private void updateSeatsAsync(ClientInfo clientInfo) {
        logger.info("Updating cinema hall adding the chosen seat");
        CompletableFuture<Void> updateCinemaHallFuture = CompletableFuture.runAsync(() -> updateCinemaHallAddingSeat(clientInfo));

        CompletableFuture.allOf(updateCinemaHallFuture)
            .thenRun(() -> logger.info("Deletion completed"))
            .exceptionally(e -> {
                e.printStackTrace();
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

    private void deleteTicketsSync(ClientInfo clientInfo) {
        Long ticketId = clientInfo.getTicketId();
        logger.info("Call transaction service to delete transaction");
        transactionsService.deleteTransaction(ticketId);
        logger.info("Deleting the ticket");
        ticketRepository.deleteTicketByTicketId(ticketId);
    }
}
