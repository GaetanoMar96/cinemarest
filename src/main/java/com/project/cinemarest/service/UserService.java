package com.project.cinemarest.service;

import com.project.cinemarest.connector.jpa.UserRepository;
import com.project.cinemarest.entity.User;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void insertUser(User user) {
        userRepository.saveAndFlush(user);
    }

    public User findByUserId(UUID userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void updateUserPassword(UUID userId, String password) {
        userRepository.updateUserPassword(userId, password);
    }
}

