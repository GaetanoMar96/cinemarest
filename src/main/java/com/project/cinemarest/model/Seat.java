package com.project.cinemarest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Seat {

    private String hallName;
    private Double baseCost;
    private String[] availableSeats;

}
