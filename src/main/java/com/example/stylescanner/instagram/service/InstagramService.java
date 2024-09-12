package com.example.stylescanner.instagram.service;

import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.instagram.dto.*;
import com.example.stylescanner.instagram.util.InstagramGraphApiUtil;
import org.springframework.data.domain.Page;
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
            List<FeedDto> feedList = instagramGraphApiUtil.GetCelebInsta(username);
            celebProfileResponseDto.setMediaCount(feedList.size());
            celebInstaResponseDto.setProfile(celebProfileResponseDto);
            celebInstaResponseDto.setFeedList(feedList);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid username");
        }

        return celebInstaResponseDto;
    }

//    public HomeFeedResponseDto readHomeFeed(FollowingListResponseDto followingList) {
//        HomeFeedResponseDto homeFeedResponseDto = new HomeFeedResponseDto();
//
//        List<FeedDto> AllFeedList = new ArrayList<>();
//
//        for(CelebProfileResponseDto celeb : followingList.getFollowing_list()){
//            String username = celeb.getProfileName();
//            try {
//                List<FeedDto> feedList = instagramGraphApiUtil.GetRecentCelebFeed(username);
//                AllFeedList.addAll(feedList);
//            } catch (IOException e) {
//                return null;
//            }
//        }
//
//        List<FeedDto> sort_AllFeedList =  AllFeedList.stream()
//                .sorted(Comparator.comparing(FeedDto::getTimestamp).reversed())
//                .collect(Collectors.toList());
//
//        homeFeedResponseDto.setFeeds(sort_AllFeedList);
//
//        return homeFeedResponseDto;
//    }


    public  List<HomeFeedResponseDto> readHomeFeed(Page<Follow> paging) throws IOException {
        List<String> followingList = paging.getContent().stream()
                .map(Follow::getFolloweeId)
                .collect(Collectors.toList());

        List<HomeFeedResponseDto> responseDtos = new ArrayList<>();
        for(String followeeId : followingList){
            HomeFeedResponseDto dto = new HomeFeedResponseDto();
            dto.setUsername(followeeId);
            dto.setProfile_url(instagramGraphApiUtil.SearchCeleb(followeeId).getProfilePictureUrl());

            FeedDto feedDto = instagramGraphApiUtil.GetRecentCelebFeed_Rapid(followeeId);

            dto.setThumbnail_url(feedDto.getMedia_url_list().get(0));
            dto.setFeed_code(feedDto.getMedia_id());

            responseDtos.add(dto);
        }

        return responseDtos;
    }

    public FeedDto readFeed(FeedRequestDto feedRequestDto) {
        String mediaId = feedRequestDto.getMedia_id();
        String feedIndex = feedRequestDto.getFeed_index();
        int feed_index = Integer.parseInt(feedIndex) % 25;
        String beforeCursor = feedRequestDto.getBefore_cursor();
        String username = feedRequestDto.getUsername();
        return instagramGraphApiUtil.GetMedia(username,  mediaId, beforeCursor,feed_index);
    }

    public List<String> findCarousel(String feed_code) throws IOException {
        return instagramGraphApiUtil.GetCarouselMedia(feed_code);
    }
}
