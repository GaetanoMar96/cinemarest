package com.project.cinemarest.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.jdbc.PgArray;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hall {

    private String hallName;

    private String idMovie;

    private Double baseCost;
    private PgArray availableSeats;
}
