package com.project.cinemarest.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo {
    private UUID userId;
    private Integer age;
    private Boolean isStudent;
    private double wallet;

    //Movie chosen by the customer
    private Long idMovie;

    private Integer seat;
}
