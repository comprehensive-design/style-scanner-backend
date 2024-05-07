package com.example.stylescanner.user.dto;

import com.example.stylescanner.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserRegisterResponseDto {
    private String email;
    private String displayName;
    private String password;
    private LocalDate birthdate;
    private Byte gender;

    public UserRegisterResponseDto(User user) {
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.password = user.getPassword();
        this.birthdate = user.getBirthdate();
        this.gender = user.getGender();
    }
}
