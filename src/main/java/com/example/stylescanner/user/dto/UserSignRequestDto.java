package com.example.stylescanner.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserSignRequestDto {
    private String email;
    private String password;
}
