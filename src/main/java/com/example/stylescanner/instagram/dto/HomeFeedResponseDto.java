package com.example.stylescanner.instagram.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeFeedResponseDto {
    List<FeedDto> feeds;
}
