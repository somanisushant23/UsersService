package com.userservice.services;

import com.userservice.dtos.*;
import com.userservice.entity.UserProfileEntity;
import com.userservice.repositories.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import static com.userservice.utils.UserServiceConstants.*;

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public ResponseEntity<SuccessResponse> createUserProfile(String email, String name, String password) {
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setName(name);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        userProfileEntity.setPassword(encryptedPassword);
        userProfileEntity.setEmail(email);
        userProfileEntity.setCreatedOn(new Date());
        userProfileEntity.setLastUpdatedOn(new Date());

        logger.info("Creating user profile: {}", userProfileEntity);
        userProfileRepository.save(userProfileEntity);
        logger.info("Profile created");
        SuccessResponse successResponse = new SuccessResponse(USER_CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }
}
