package com.project.cinemarest.service;

import com.project.cinemarest.connector.jpa.UserRepository;
import com.project.cinemarest.entity.User;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
