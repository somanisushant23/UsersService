package com.userservice.dtos;

import lombok.Getter;

@Getter
public class SuccessResponse extends ApiResponse {

    public SuccessResponse(String message) {
        this.message = message;
    }
}
