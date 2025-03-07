package com.userservice.repositories;

import com.userservice.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    boolean existsByEmail(String email);
}
