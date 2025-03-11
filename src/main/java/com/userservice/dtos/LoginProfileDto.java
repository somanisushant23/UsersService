package com.userservice.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginProfileDto {
    private String email;

    private String password;
}
