package com.project.cinemarest.service;

import com.project.cinemarest.connector.jpa.TransactionRepository;
import com.project.cinemarest.exception.SqlConnectionException;
import com.project.cinemarest.entity.Transaction;
import java.util.UUID;
import javax.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public void insertTransaction(Transaction transaction) {
        try {
            transaction.setTransactionId(UUID.randomUUID());
            transactionRepository.saveAndFlush(transaction);
        } catch (PersistenceException exception) {
            deleteTransaction(transaction.getTicketId());
            throw new SqlConnectionException("Error while inserting transaction");
        }
    }

    public void deleteTransaction(Long ticketId) {
        transactionRepository.deleteTransaction(ticketId);
    }

    public void increaseUserWallet(String userId, Double price) {
        userService.updateUserWallet(price, UUID.fromString(userId));
    }
}
