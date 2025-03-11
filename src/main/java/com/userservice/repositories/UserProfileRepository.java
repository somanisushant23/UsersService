package com.userservice.repositories;

import com.userservice.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    boolean existsByEmail(String email);

    Optional<UserProfileEntity> findByEmail(String email);
}
