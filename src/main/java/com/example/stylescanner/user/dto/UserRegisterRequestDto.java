package com.example.stylescanner.user.dto;

import com.example.stylescanner.user.entity.User;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegisterRequestDto {
    private String email;
    private String displayName;
    private String password;
    private LocalDate birthdate;
    private Byte gender;

    public User toEntity(){
        return User.builder()
                .email(email)
                .displayName(displayName)
                .password(password)
                .birthdate(birthdate)
                .gender(gender)
                .build();
    }

}
