package com.example.stylescanner.user.dto;

import com.example.stylescanner.user.entity.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateInfoDto {
    private String displayName;
    private String bio;
    private String password;
    private LocalDate birthdate;
    private Byte gender;
    private String profilePictureUrl;


    public User toEntity(){
        return User.builder()
                .displayName(displayName)
                .bio(bio)
                .password(password)
                .birthdate(birthdate)
                .gender(gender)
                .profilePictureUrl(profilePictureUrl)
                .build();
    }
}
