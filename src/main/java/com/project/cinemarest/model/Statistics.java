package com.project.cinemarest.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {

    private String name;
    private BigDecimal seats;
    private Float ageAverage;
    private Float accumulatedMoney;

}
