package com.project.cinemarest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.jdbc.PgArray;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TCinemaSeat {

    private Double baseCost;
    private PgArray availableSeats;
}
