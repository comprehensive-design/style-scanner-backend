package com.example.stylescanner.follow.dto;

import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CelebRankingResponseDto {
    private String profilePictureUrl;
    private String profileName;
    private int profileFollowerCount;
    private int profileFollowingCount;
    private String profileBio;
    private int mediaCount;
    String feed_url;

    public CelebRankingResponseDto(CelebProfileResponseDto celebProfile ,String feed_url) {
        this.profilePictureUrl = celebProfile.getProfilePictureUrl();
        this.profileName = celebProfile.getProfileName();
        this.profileFollowerCount = celebProfile.getProfileFollowerCount();
        this.profileFollowingCount = celebProfile.getProfileFollowingCount();
        this.profileBio = celebProfile.getProfileBio();
        this.mediaCount = celebProfile.getMediaCount();
        this.feed_url = feed_url;
    }
}
