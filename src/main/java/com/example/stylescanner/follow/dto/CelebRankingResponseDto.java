package com.example.stylescanner.follow.dto;

import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CelebRankingResponseDto {
    CelebProfileResponseDto profile;
    Long followingCount;
    String feed_url;

    public CelebRankingResponseDto(CelebProfileResponseDto celebProfile, Long followingCount, String feed_url) {
        this.profile = celebProfile;
        this.followingCount = followingCount;
        this.feed_url = feed_url;
    }
}
