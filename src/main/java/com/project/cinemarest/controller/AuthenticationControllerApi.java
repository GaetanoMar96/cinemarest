package com.project.cinemarest.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.project.cinemarest.model. AuthenticationRequest;
import com.project.cinemarest.model.AuthenticationResponse;
import com.project.cinemarest.model.PasswordRequest;
import com.project.cinemarest.model.RegisterRequest;
import com.project.cinemarest.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cinema/auth")
@RequiredArgsConstructor
public class AuthenticationControllerApi {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ok(authenticationService.authenticate(request));
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordRequest request) {
        authenticationService.changePassword(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
