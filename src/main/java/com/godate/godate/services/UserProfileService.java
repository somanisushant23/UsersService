package com.godate.godate.services;

import com.godate.godate.dtos.*;
import com.godate.godate.entity.UserProfileEntity;
import com.godate.godate.repositories.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import static com.godate.godate.utils.UserServiceConstants.*;

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public ResponseEntity<SuccessResponse> createUserProfile(UserProfileDto userProfileDto) {
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setPhoneNumber(userProfileDto.getPhoneNumber());
        userProfileEntity.setName(userProfileDto.getName());
        userProfileEntity.setGender(userProfileDto.getGender());
        userProfileEntity.setCreatedOn(new Date());
        userProfileEntity.setLastUpdatedOn(new Date());
        userProfileEntity.setActive(true);

        logger.info("Creating user profile: {}", userProfileEntity);
        userProfileRepository.save(userProfileEntity);
        logger.info("Profile created");
        SuccessResponse successResponse = new SuccessResponse(USER_CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    public ResponseEntity<ApiResponse> updateUserProfile(UserProfileUpdateDto userProfileUpdateDto) {
        logger.info("Updating user profile: {}", userProfileUpdateDto);
        if(userProfileRepository.existsByPhoneNumber(userProfileUpdateDto.getPhoneNumber())) {
            userProfileRepository.updateUserName(userProfileUpdateDto.getPhoneNumber(), userProfileUpdateDto.getName(), new Date());
            logger.info("User profile updated");
            SuccessResponse successResponse = new SuccessResponse(USER_UPDATED);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(USER_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
