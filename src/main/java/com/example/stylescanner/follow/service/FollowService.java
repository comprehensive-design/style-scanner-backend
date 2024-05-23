package com.example.stylescanner.follow.service;

import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.follow.dto.FollowingRequestDto;
import com.example.stylescanner.follow.dto.UnFollowingRequestDto;
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
             celebProfileList.add(celebProfileResponseDto);
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
}
