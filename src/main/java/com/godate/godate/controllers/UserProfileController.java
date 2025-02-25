package com.godate.godate.controllers;

import com.godate.godate.dtos.ApiResponse;
import com.godate.godate.dtos.SuccessResponse;
import com.godate.godate.dtos.UserProfileDto;
import com.godate.godate.dtos.UserProfileUpdateDto;
import com.godate.godate.services.UserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createUserProfile(@Valid @RequestBody UserProfileDto userProfileDto) {
        logger.info("Creating user profile {}", userProfileDto.getPhoneNumber());
        return userProfileService.createUserProfile(userProfileDto);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> updateUserProfile(@Valid @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        logger.info("Updating user profile {}", userProfileUpdateDto.getPhoneNumber());
        return userProfileService.updateUserProfile(userProfileUpdateDto);
    }
}
