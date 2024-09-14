package com.example.stylescanner.instagram.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

//@Data
//public class HomeFeedResponseDto {
//    List<FeedDto> feeds;
//}

@Data
public class HomeFeedResponseDto {
    String username;
    String profile_url;
    String thumbnail_url;
    String feed_code;
    int carousel_count;
}