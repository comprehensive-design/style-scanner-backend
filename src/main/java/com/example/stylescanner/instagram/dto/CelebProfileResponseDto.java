package com.example.stylescanner.instagram.dto;

import lombok.Data;

@Data
public class CelebProfileResponseDto {
    private String profilePictureUrl;
    private String profileName;
    private int profileFollowerCount;
}
