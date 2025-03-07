package com.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users_profile")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity extends BaseModelEntity {

    @Column(nullable = false)
    private String name;

    private String email;

    private String password;
}
