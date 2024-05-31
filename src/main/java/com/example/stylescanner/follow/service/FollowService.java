package com.example.stylescanner.follow.service;

import com.example.stylescanner.follow.dto.*;
import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.follow.repository.FollowRepository;
import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import com.example.stylescanner.instagram.util.InstagramGraphApiUtil;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    @Autowired
    private final InstagramGraphApiUtil instagramGraphApiUtil;
    @Autowired
    private UserRepository userRepository;

    public FollowService(UserRepository userRepository, FollowRepository followRepository, InstagramGraphApiUtil instagramGraphApiUtil) {
        this.followRepository = followRepository;
        this.instagramGraphApiUtil = instagramGraphApiUtil;
    }

    public List<Follow> list() {
        return followRepository.findAll();
    }

    public Follow read(Long id) {
        return followRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found follow"));
    }

    public CelebProfileResponseDto search(String keyword) {
        try {
            if(instagramGraphApiUtil.SearchCeleb(keyword) == null){
                return null;
            }else{
                return instagramGraphApiUtil.SearchCeleb(keyword);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean follow(String email, FollowingRequestDto requestDto) {
        User user =  userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found user"));

        try {
            if(instagramGraphApiUtil.SearchCeleb(requestDto.getFolloweeId()) == null){
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //중복 팔로잉 방지
        if(followRepository.existsByFolloweeIdAndUser(requestDto.getFolloweeId(),user)){
            return false;
        }else {
            Follow follow = Follow.builder()
                    .followeeId(requestDto.getFolloweeId())
                    .user(user)
                    .build();

            followRepository.save(follow);
            return true;
        }
    }

    public FollowingListResponseDto followingList(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found user"));

        List<Follow> follows = followRepository.findByUser(user);
        System.out.println(follows);
        FollowingListResponseDto responseDto = new FollowingListResponseDto();
        List<CelebProfileResponseDto> celebProfileList = new ArrayList<>();

        for (Follow follow : follows) {
            String followeeId = follow.getFolloweeId();
            CelebProfileResponseDto celebProfileResponseDto = null;
            try {
                celebProfileResponseDto = instagramGraphApiUtil.SearchCeleb(followeeId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(celebProfileResponseDto != null){
                celebProfileList.add(celebProfileResponseDto);
            }
        }
        responseDto.setFollowing_list(celebProfileList);

        return responseDto;
    }

    public Boolean unfollow(String email, UnFollowingRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found user"));

        String followeeId = requestDto.getFolloweeId();
        Follow follow = followRepository.findByUserAndFolloweeId(user, followeeId).orElseThrow(() -> new IllegalArgumentException("not found follow"));

        followRepository.delete(follow);

        return true;
    }

    public Boolean checkFollowing(String email, String keyword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found user"));

        if(followRepository.existsByFolloweeIdAndUser(keyword, user)){
            return false;
        }else{
            return true;
        }
    }

    public List<RecommendResponseDto> recommend(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found user"));

        List<Follow> userFollows = followRepository.findByUser(user);
        List<String> userFolloweeIds = userFollows.stream().map(Follow::getFolloweeId)
                .collect(Collectors.toList());

        List<Map<String, Object>> recommendedFolloweesWithCount = followRepository.findRecommendedFolloweesWithCount(userFolloweeIds, user);

        List<String> recommendList = recommendedFolloweesWithCount.stream()
                .map(result -> (String) result.get("followeeId"))
                .collect(Collectors.toList());

        List<RecommendResponseDto> recommendedResponseDtoList = new ArrayList<>();

        int cnt = 0;
        for(String celeb_id : recommendList){
            if(cnt>6) break;
            CelebProfileResponseDto profile_info =  search(celeb_id);
            RecommendResponseDto recommendedResponseDto = new RecommendResponseDto();
            recommendedResponseDto.setProfileName(celeb_id);
            recommendedResponseDto.setProfilePictureUrl(profile_info.getProfilePictureUrl());
            recommendedResponseDto.setProfileFollowerCount(profile_info.getProfileFollowerCount());

            //최근 3 피드 이미지 가져오기
            try {
                List<String> media_list =  instagramGraphApiUtil.GetRecentCelebFeed(celeb_id, 3);
                recommendedResponseDto.setFeed_3_list(media_list);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            recommendedResponseDtoList.add(recommendedResponseDto);
            cnt++;
        }
        return recommendedResponseDtoList;
    }

    public List<CelebRankingResponseDto> ranking() {
        List<Object[]> celebRankingList = followRepository.findTopFollowedCelebrities();
        List<CelebRankingResponseDto> celebRankingResponseDtoList = celebRankingList.stream().limit(5).map(result -> new CelebRankingResponseDto((CelebProfileResponseDto) search((String) result[0]), (Long) result[1])).collect(Collectors.toList());
        return celebRankingResponseDtoList;
    }
}
