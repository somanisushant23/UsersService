package com.userservice.services;

import com.userservice.dtos.*;
import com.userservice.entity.Role;
import com.userservice.entity.UserProfileEntity;
import com.userservice.entity.enums.UserRole;
import com.userservice.exceptions.IncorrectDataException;
import com.userservice.repositories.RoleRepository;
import com.userservice.repositories.UserProfileRepository;
import com.userservice.utils.auth.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;

import static com.userservice.utils.UserServiceConstants.*;

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProfileService(UserProfileRepository userProfileRepository, RoleRepository roleRepository,
                              JwtUtils jwtUtils, RedisTemplate<String, Object> redisTemplate) {
        this.userProfileRepository = userProfileRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.redisTemplate = redisTemplate;
    }

    public ResponseEntity<SuccessResponse> createUserProfile(String email, String name, String password) {
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setName(name);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        userProfileEntity.setPassword(encryptedPassword);
        userProfileEntity.setEmail(email);
        userProfileEntity.setCreatedOn(new Date());
        userProfileEntity.setLastUpdatedOn(new Date());
        userProfileEntity.setRoles(getExistingRoles());

        logger.info("Creating user profile: {}", userProfileEntity);
        userProfileRepository.save(userProfileEntity);
        logger.info("Profile created");
        SuccessResponse successResponse = new SuccessResponse(USER_CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    private Set<Role> getExistingRoles() {
        Role role = roleRepository.findByRole("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
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
            // store user data in redis cache
            redisTemplate.opsForHash().put("USERS", user.getEmail(), userProfileResponse);
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
