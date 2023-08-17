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

    @Modifying
    @Query("update User u set u.wallet = :price where u.userId = :id")
    int updateUserWallet(double price, UUID id);

    @Modifying
    @Query("update User u set u.age = :age, u.isStudent = :isSTudent where u.userId = :id")
    int updateUserInfo(UUID id, Integer age, Boolean isStudent);
}
