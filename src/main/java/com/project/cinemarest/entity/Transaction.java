package com.project.cinemarest.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_cinema_transaction")
public class Transaction {

    @Id
    private UUID transactionId;

    private Long ticketId;

    private String idMovie;

    private UUID userId;
}
