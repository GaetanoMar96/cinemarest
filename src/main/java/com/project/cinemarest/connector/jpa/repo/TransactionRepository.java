package com.project.cinemarest.connector.jpa.repo;

import com.project.cinemarest.entity.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Modifying
    @Query("delete from Transaction t where t.ticketId = :ticketId")
    int deleteTransaction(Long ticketId);
}
