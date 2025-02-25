package com.godate.godate.repositories;

import com.godate.godate.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    @Modifying
    @Transactional
    @Query("update UserProfileEntity u SET u.name = :name, u.lastUpdatedOn = :lastUpdatedOn where u.phoneNumber = :phoneNumber")
    int updateUserName(@Param("phoneNumber") String phoneNumber, @Param("name") String name, @Param("lastUpdatedOn") Date lastUpdatedOn);

    boolean existsByPhoneNumber(String phoneNumber);
}
