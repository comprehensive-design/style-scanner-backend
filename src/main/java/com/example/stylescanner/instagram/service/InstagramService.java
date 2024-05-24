package com.example.stylescanner.instagram.service;

import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.instagram.dto.*;
import com.example.stylescanner.instagram.util.InstagramGraphApiUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstagramService {
    private final InstagramGraphApiUtil instagramGraphApiUtil;

    public InstagramService(InstagramGraphApiUtil instagramGraphApiUtil) {
        this.instagramGraphApiUtil = instagramGraphApiUtil;
    }

    public CelebInstaResponseDto readCelebInsta(String username) {
        CelebInstaResponseDto celebInstaResponseDto = new CelebInstaResponseDto();
        try {
            CelebProfileResponseDto celebProfileResponseDto = instagramGraphApiUtil.SearchCeleb(username);
            List<FeedDto> feedList = instagramGraphApiUtil.GetALlCelebFeed(username);
            celebProfileResponseDto.setMediaCount(feedList.size());
            celebInstaResponseDto.setProfile(celebProfileResponseDto);
            celebInstaResponseDto.setFeedList(feedList);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid username");
        }

        return celebInstaResponseDto;
    }

    public HomeFeedResponseDto readHomeFeed(FollowingListResponseDto followingList) {
        HomeFeedResponseDto homeFeedResponseDto = new HomeFeedResponseDto();

        List<FeedDto> AllFeedList = new ArrayList<>();

        for(CelebProfileResponseDto celeb : followingList.getFollowing_list()){
            String username = celeb.getProfileName();
            try {
                List<FeedDto> feedList = instagramGraphApiUtil.GetRecentCelebFeed(username);
                AllFeedList.addAll(feedList);
            } catch (IOException e) {
                return null;
            }
        }

        List<FeedDto> sort_AllFeedList =  AllFeedList.stream()
                .sorted(Comparator.comparing(FeedDto::getTimestamp).reversed())
                .collect(Collectors.toList());

        homeFeedResponseDto.setFeeds(sort_AllFeedList);

        return homeFeedResponseDto;
    }
}
