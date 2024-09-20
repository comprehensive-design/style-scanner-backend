package com.example.stylescanner.instagram.dto;

import lombok.Data;

@Data
public class HomeFeedResponseDto {
    String username;
    String profile_url;
    String thumbnail_url;
    String feed_code;
    int carousel_count;
}