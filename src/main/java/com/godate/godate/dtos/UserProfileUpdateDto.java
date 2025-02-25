package com.godate.godate.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileUpdateDto {

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Size(min = 3, max = 30, message = "Name should be more than 3 & less than 10 characters")
    private String name;
}
