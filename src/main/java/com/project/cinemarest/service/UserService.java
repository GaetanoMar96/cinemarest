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

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void updateUserWallet(Double price, UUID userId) {
        userRepository.updateUserWalletAfterPayment(price, userId);
    }
}
