package com.example.stylescanner.follow.dto;

import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class FollowingListResponseDto {
    private List<CelebProfileResponseDto> following_list;
    private int followingCount;
}
