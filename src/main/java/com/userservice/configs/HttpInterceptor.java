package com.userservice.configs;

import com.userservice.utils.auth.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);
    private String requestId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        requestId = UUID.randomUUID().toString();
        response.setHeader("requestId", requestId);

        if(shouldSkipAuth(request)) {
            return true;
        } else {
            boolean isValid = isValid(request.getHeader("Authorization"), request.getHeader("email"));
            logger.info("Token is {}", isValid);
            if(!isValid) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Auth failed!!");
                return false;
            }
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        logger.info("API [{} {}] took {} ms", request.getMethod(), request.getRequestURI(), duration);
    }

    private boolean isValid(String token, String headerEmail) {
        boolean isTokenValid = jwtUtils.validateToken(token, headerEmail);
        logger.info("Token isValid: {}", isTokenValid);
        return isTokenValid;
    }

    private boolean shouldSkipAuth(HttpServletRequest request) {
        if(request.getRequestURL().toString().contains("profile/signup") ||
                request.getRequestURL().toString().contains("profile/signin")) {
            return true;
        }
        return false;
    }
}
