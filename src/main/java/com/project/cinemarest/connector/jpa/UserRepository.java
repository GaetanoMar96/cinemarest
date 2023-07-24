package com.project.cinemarest.connector.jpa;

import com.project.cinemarest.entity.UserInfo;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, UUID> {

    Optional<UserInfo> findByEmail(String email);
}
