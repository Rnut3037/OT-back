package com.onetouch.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.onetouch.users.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserId(String userId);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}

