package com.userservice.controllers;

import com.userservice.dtos.ApiResponse;
import com.userservice.dtos.SuccessResponse;
import com.userservice.dtos.UserProfileDto;
import com.userservice.dtos.UserProfileUpdateDto;
import com.userservice.services.UserProfileService;
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
        logger.info("Creating user profile {}", userProfileDto.getEmail());
        return userProfileService.createUserProfile(userProfileDto.getEmail(), userProfileDto.getName(), userProfileDto.getPassword());
    }
}
