package com.project.cinemarest.connector.jpa;

import com.project.cinemarest.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(UUID userId);

    @Modifying
    @Query("update User u set u.password = :password where u.userId = :userId")
    void updateUserPassword(UUID userId, String password);
}
