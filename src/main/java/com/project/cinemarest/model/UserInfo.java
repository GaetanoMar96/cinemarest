package com.project.cinemarest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String name;
    private String surname;

    private String email;

    private String password; //da decriptare
    private String phone;
    private double wallet;
}
