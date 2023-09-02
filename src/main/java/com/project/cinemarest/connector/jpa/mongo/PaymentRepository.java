package com.project.cinemarest.connector.jpa.mongo;

import com.project.cinemarest.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    void deleteByTicketId(Long ticketId);
}
