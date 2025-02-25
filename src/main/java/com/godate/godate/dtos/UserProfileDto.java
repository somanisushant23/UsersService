package com.godate.godate.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileDto {

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Size(min = 3, max = 30, message = "Name should be more than 3 & less than 10 characters")
    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 0, message = "Gender must be 0 (male) or 1 (female)")
    @Max(value = 1, message = "Gender must be 0 (male) or 1 (female)")
    private int gender;
}
