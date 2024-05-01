package com.example.campuscart.repositories.userservice;

import com.example.campuscart.entities.userservice.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
