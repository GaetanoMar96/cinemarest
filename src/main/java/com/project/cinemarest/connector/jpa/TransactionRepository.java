package com.project.cinemarest.connector.jpa;

import com.project.cinemarest.entity.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Modifying
    void deleteTransactionByTicketId(Long ticketId);
}
