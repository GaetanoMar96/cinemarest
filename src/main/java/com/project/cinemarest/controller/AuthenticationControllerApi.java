package com.project.cinemarest.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.project.cinemarest.model. AuthenticationRequest;
import com.project.cinemarest.model.AuthenticationResponse;
import com.project.cinemarest.model.RegisterRequest;
import com.project.cinemarest.service.AuthenticationService;
import com.project.cinemarest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cinema/auth")
@RequiredArgsConstructor
public class AuthenticationControllerApi {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
