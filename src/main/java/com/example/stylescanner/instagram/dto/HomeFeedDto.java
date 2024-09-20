package com.example.stylescanner.instagram.dto;


import lombok.Data;

import java.util.List;

@Data
public class HomeFeedDto {
    List<HomeFeedResponseDto> homeFeedResponseDtoList;
    int total_count;
}
