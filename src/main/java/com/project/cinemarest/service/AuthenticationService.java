package com.project.cinemarest.service;

import com.project.cinemarest.entity.User;
import com.project.cinemarest.model.AuthenticationRequest;
import com.project.cinemarest.model.AuthenticationResponse;
import com.project.cinemarest.model.RegisterRequest;
import com.project.cinemarest.security.JwtService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
            .userId(UUID.randomUUID())
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            //.wallet(0) //at registration the wallet is empty
            .role(request.getRole())
            .build();
        String jwtToken = jwtService.generateToken(user);
        userService.insertUser(user);
        return AuthenticationResponse.builder()
            .userId(user.getUserId())
            .accessToken(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        User user = userService.findByEmail(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .userId(user.getUserId())
            .accessToken(jwtToken)
            .build();
    }
}
