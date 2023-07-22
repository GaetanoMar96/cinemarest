package com.project.cinemarest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Seat {

    private Double baseCost;
    private Integer[] availableSeats;

}
