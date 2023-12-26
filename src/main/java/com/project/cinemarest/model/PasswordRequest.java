package com.project.cinemarest.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

    private UUID userId;
    private String oldPassword;
    private String newPassword;
}
