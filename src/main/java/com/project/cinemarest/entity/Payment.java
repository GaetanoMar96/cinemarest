package com.project.cinemarest.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private UUID userId;
    private Long ticketId;
    private UUID transactionId;
    private String cardNumber;
    private String expirationDate;
    private String cvc;
    private Double totalPrice;
    private LocalDateTime createdAt;
}
