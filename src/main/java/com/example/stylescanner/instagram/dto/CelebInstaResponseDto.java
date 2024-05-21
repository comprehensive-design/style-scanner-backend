package com.example.stylescanner.instagram.dto;

import lombok.Data;
import java.util.List;

@Data
public class CelebInstaResponseDto {
    private CelebProfileResponseDto profile;
    private List<FeedDto> feedList;
}
