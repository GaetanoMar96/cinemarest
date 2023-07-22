package com.project.cinemarest.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TCinemaClient {

    private UUID idClient;

    //Movie chosen by the customer
    private Long idMovie;

    //ticket for the chosen movie
    private Long ticketId;

    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String telephone;
}


