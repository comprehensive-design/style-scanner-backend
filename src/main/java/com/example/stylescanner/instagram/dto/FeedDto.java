package com.example.stylescanner.instagram.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedDto {
    List<String> media_url_list;
    String username;
    String profile_url;
    LocalDateTime timestamp;
    String media_id;
}
