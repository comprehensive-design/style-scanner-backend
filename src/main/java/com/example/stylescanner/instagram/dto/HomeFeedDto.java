package com.example.stylescanner.instagram.dto;


import lombok.Data;

import java.util.List;

@Data
public class HomeFeedDto {
    List<HomeFeedResponseDto> homeFeedList;
    int total_count;
}
