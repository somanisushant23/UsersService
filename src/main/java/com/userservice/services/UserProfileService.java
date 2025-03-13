package com.userservice.services;

import com.userservice.dtos.*;
import com.userservice.entity.UserProfileEntity;
import com.userservice.exceptions.IncorrectDataException;
import com.userservice.repositories.UserProfileRepository;
import com.userservice.utils.auth.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.userservice.utils.UserServiceConstants.*;

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    private final JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProfileService(UserProfileRepository userProfileRepository, JwtUtils jwtUtils) {
        this.userProfileRepository = userProfileRepository;
        this.jwtUtils = jwtUtils;
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

    public UserProfileResponse loginUser(String email, String password) throws IncorrectDataException, HttpServerErrorException {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findByEmail(email);
        if(userProfileEntity.isEmpty()) {
            logger.info("User not found during login!!");
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }
        UserProfileEntity user = userProfileEntity.get();
        if(bCryptPasswordEncoder.matches(password, user.getPassword())) {
            logger.info("User logged in {}", user.getEmail());
            //share Jwt token
            UserProfileResponse userProfileResponse = new UserProfileResponse();
            userProfileResponse.setEmail(user.getEmail());
            userProfileResponse.setName(user.getName());
            String token = jwtUtils.createToken(user.getEmail(), getClaims(user.getName()));
            userProfileResponse.setToken(token);
            return userProfileResponse;

        } else {
            // throw incorrect password exception
            throw new IncorrectDataException(USER_NOT_AUTHORIZED);
        }
    }

    public ResponseEntity<UserProfileResponse> getUserProfile(String email) {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findByEmail(email);
        if(userProfileEntity.isEmpty()) {
            //throw 404 exception
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        } else {
            UserProfileEntity userProfileEntityResp = userProfileEntity.get();
            UserProfileResponse userProfileResponse = new UserProfileResponse();
            userProfileResponse.setName(userProfileEntityResp.getName());
            userProfileResponse.setEmail(userProfileEntityResp.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(userProfileResponse);
        }
    }

    private Map<String, String> getClaims(String name) {
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "end_user");
        claims.put("name", name);
        return claims;
    }
}
