package com.project.cinemarest.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo {

    private String name;
    private String surname;
    private BigDecimal age;
    private Boolean isStudent;
    private Float wallet;
    private String email;
    private String telephone;
}
