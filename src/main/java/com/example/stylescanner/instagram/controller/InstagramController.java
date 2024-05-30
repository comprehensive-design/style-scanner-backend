package com.example.stylescanner.instagram.controller;

import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.follow.service.FollowService;
import com.example.stylescanner.instagram.api.InstagramApi;
import com.example.stylescanner.instagram.dto.CelebInstaResponseDto;
import com.example.stylescanner.instagram.dto.FeedDto;
import com.example.stylescanner.instagram.dto.FeedRequestDto;
import com.example.stylescanner.instagram.dto.HomeFeedResponseDto;
import com.example.stylescanner.instagram.service.InstagramService;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InstagramController implements InstagramApi {
    private final InstagramService instagramService;
    private final FollowService followService;
    private final JwtProvider jwtProvider;

    @Override
    public CelebInstaResponseDto readCelebInsta(String username) {
        return instagramService.readCelebInsta(username);
    }

    @Override
    public HomeFeedResponseDto getHomeFeed(HttpServletRequest request) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        FollowingListResponseDto followingList =  followService.followingList(email);
        return instagramService.readHomeFeed(followingList);
    }

    @Override
    public FeedDto getFeed(FeedRequestDto feedRequestDto) {
        return instagramService.readFeed(feedRequestDto);
    }
}
