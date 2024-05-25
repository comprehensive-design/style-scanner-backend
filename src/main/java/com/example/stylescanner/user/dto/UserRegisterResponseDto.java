package com.example.stylescanner.user.dto;

import com.example.stylescanner.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserRegisterResponseDto {
    private Long id;
    private String email;
    private String password;
    private String displayName;
    private String birthdate;
    private Byte gender;
    private String profilePictureUrl;
    private String bio;
    private String createdAt;

    @Builder
    public UserRegisterResponseDto(User user) {
        this.id=user.getId();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.password = user.getPassword();
        this.birthdate = String.valueOf(user.getBirthdate());
        this.gender = user.getGender();
        this.bio = user.getBio();
        this.createdAt = user.getCreatedAt().toString();
        this.profilePictureUrl = user.getProfilePictureUrl();
    }

}
