package com.project.cinemarest.connector.jpa.mongo;

import com.project.cinemarest.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    @Modifying
    void deleteByTicketId(Long ticketId);
}
