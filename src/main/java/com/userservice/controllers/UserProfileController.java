package com.userservice.controllers;

import com.userservice.dtos.*;
import com.userservice.entity.UserProfileEntity;
import com.userservice.exceptions.IncorrectDataException;
import com.userservice.services.UserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("signup")
    public ResponseEntity<SuccessResponse> createUserProfile(@Valid @RequestBody UserProfileDto userProfileDto) {
        logger.info("Creating user profile {}", userProfileDto.getEmail());
        return userProfileService.createUserProfile(userProfileDto.getEmail(), userProfileDto.getName(), userProfileDto.getPassword());
    }

    @PostMapping("signin")
    public UserProfileResponse loginUser(@Valid @RequestBody LoginProfileDto loginProfileDto) throws IncorrectDataException, HttpServerErrorException {
        logger.info("Login in user {}", loginProfileDto.getEmail());
        return userProfileService.loginUser(loginProfileDto.getEmail(), loginProfileDto.getPassword());
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestHeader("email") String email) {
        logger.info("Get user profile {}", email);
        return userProfileService.getUserProfile(email);
    }
}
