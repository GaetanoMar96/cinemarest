package com.project.cinemarest.connector.jpa.repo;

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
    int updateUserPassword(UUID userId, String password);
}
