package com.example.stylescanner.follow.dto;

import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CelebRankingResponseDto {
    CelebProfileResponseDto profile;
    Long followingCount;

    public CelebRankingResponseDto(CelebProfileResponseDto celebProfile, Long followingCount) {
        this.profile = celebProfile;
        this.followingCount = followingCount;
    }
}
