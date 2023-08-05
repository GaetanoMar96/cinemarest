package com.project.cinemarest.service;

import com.project.cinemarest.connector.jdbc.utils.JdbcQuery.JdbcQueryBuilder;
import com.project.cinemarest.model.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DeleteService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteService.class);

    public ResponseEntity<Void> deleteMovieTicket(ClientInfo clientInfo) {
        logger.info("Updating cinema hall adding the chosen seat");

        logger.info("Call transaction controller to delete transaction");

        logger.info("Deleting ticket inside table");

        logger.info("Updating client wallet");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void deleteMovieTicket(Long ticketId) {

    }

    private void updateCinemaHallAddingSeat() {

    }
}
