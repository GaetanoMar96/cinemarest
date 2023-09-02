package com.project.cinemarest.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.project.cinemarest.CoreTestSpringConfiguration;
import com.project.cinemarest.exception.SqlConnectionException;
import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.connector.jdbc.QueryJdbcConnector;
import com.project.cinemarest.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {CoreTestSpringConfiguration.class})
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("unittest")
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @SpyBean
    private TransactionsService transactionsService;

    @MockBean
    private QueryJdbcConnector jdbcConnector;

    @Test
    void postMovieTicketKo() {
        Mockito.when(jdbcConnector.nextVal("cinema.seq_cinema_ticket"))
            .thenThrow(new SqlConnectionException("Error"));
        ClientInfo clientInfo = TestUtils.createClient();
        assertThrows(SqlConnectionException.class,
                     () -> this.ticketService.postMovieTicket(clientInfo));
    }

    @Test
    void insertMovieTicketKo() {
        Mockito.when(jdbcConnector.nextVal("cinema.seq_cinema_ticket"))
            .thenReturn(1L);
        Mockito.doThrow(new SqlConnectionException("Error")).when(jdbcConnector).insert(any());
        ClientInfo clientInfo = TestUtils.createClient();
        assertThrows(InvalidDataAccessResourceUsageException.class,
                     () -> this.ticketService.postMovieTicket(clientInfo));
    }

    @Test
    void callTransactionServiceKo() {
        Mockito.when(jdbcConnector.nextVal("cinema.seq_cinema_ticket"))
            .thenReturn(1L);
        Mockito.doNothing().when(jdbcConnector).insert(any());
        Mockito.doThrow(new SqlConnectionException("Error")).when(transactionsService).insertTransaction(any());
        ClientInfo clientInfo = TestUtils.createClient();
        assertThrows(InvalidDataAccessResourceUsageException.class,
                     () -> this.ticketService.postMovieTicket(clientInfo));
    }

    @Test
    void updateCinemaHallKo() {
        Mockito.when(jdbcConnector.nextVal("cinema.seq_cinema_ticket"))
            .thenReturn(1L);
        Mockito.doNothing().when(jdbcConnector).insert(any());
        Mockito.doNothing().when(transactionsService).insertTransaction(any());
        Mockito.doThrow(new SqlConnectionException("Error")).when(jdbcConnector).update(any());
        Mockito.doNothing().when(jdbcConnector).delete(any());
        ClientInfo clientInfo = TestUtils.createClient();
        assertThrows(InvalidDataAccessResourceUsageException.class,
                     () -> this.ticketService.postMovieTicket(clientInfo));
    }

}
