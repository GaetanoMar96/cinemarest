package com.project.cinemarest.entity;

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
@Table(name = "t_cinema_ticket")
public class Ticket {

    @Id
    private Long ticketId;

    private String idMovie;

    private Double cost;
}
