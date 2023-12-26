package com.project.cinemarest.service;

import com.project.cinemarest.connector.jpa.TransactionRepository;
import com.project.cinemarest.exception.SqlConnectionException;
import com.project.cinemarest.entity.Transaction;
import javax.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;

    public void insertTransaction(Transaction transaction) {
        try {
            transactionRepository.saveAndFlush(transaction);
        } catch (PersistenceException exception) {
            throw new SqlConnectionException("Error while inserting transaction");
        }
    }

    @Transactional
    public void deleteTransaction(Long ticketId) {
        transactionRepository.deleteTransactionByTicketId(ticketId);
    }

}
