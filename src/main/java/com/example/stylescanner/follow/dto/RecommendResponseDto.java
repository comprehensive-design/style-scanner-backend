package com.example.stylescanner.follow.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecommendResponseDto {
    private String profilePictureUrl;
    private String profileName;
    private int profileFollowerCount;
    private List<String> feed_3_list;
}
