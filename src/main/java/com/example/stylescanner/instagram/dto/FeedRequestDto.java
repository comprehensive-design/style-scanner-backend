package com.example.stylescanner.instagram.dto;

import lombok.Data;

@Data
public class FeedRequestDto {
    String username;
    String before_cursor;
    String media_id;
    String feed_index;
}
