package com.project.cinemarest.service;

import com.project.cinemarest.connector.jpa.repo.UserRepository;
import com.project.cinemarest.entity.User;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void updateUserWallet(Double price, UUID userId) {
        userRepository.updateUserWallet(price, userId);
    }

    @Transactional
    public ResponseEntity<Void> updateUserInfo(String userId, Integer age, Boolean isStudent) {
        userRepository.updateUserInfo(UUID.fromString(userId), age, isStudent);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

