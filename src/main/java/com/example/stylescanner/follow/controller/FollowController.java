package com.example.stylescanner.follow.controller;

import com.example.stylescanner.follow.api.FollowApi;
import com.example.stylescanner.follow.dto.*;
import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.follow.service.FollowService;
import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowApi {
    private final FollowService followService;
    private final JwtProvider jwtProvider;

    @Override
    public List<Follow> list() {
        return followService.list();
    }

    @Override
    public ResponseEntity<Follow> read(Long id) {
        return ResponseEntity.ok(followService.read(id));
    }

    @Override
    public ResponseEntity<CelebProfileResponseDto> search(String keyword)
    {
        return ResponseEntity.ok(followService.search(keyword));
    }

    @Override
    public ResponseEntity<Boolean> follow(HttpServletRequest request, FollowingRequestDto requestDto) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));

        return ResponseEntity.ok(followService.follow(email,requestDto));
    }

    @Override
    public ResponseEntity<FollowingListResponseDto> followingList(HttpServletRequest request) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return ResponseEntity.ok(followService.followingList(email));
    }

    @Override
    public ResponseEntity<FollowingListResponseDto> followingListPaging(HttpServletRequest httpServletRequest, int page, int size) throws IOException {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(httpServletRequest).substring(7));

        Page<Follow> follows = followService.followingListPaging(email,page,size);

        FollowingListResponseDto followingListResponseDto = followService.followingListPaging(follows);

        return ResponseEntity.ok(followingListResponseDto);
    }

    @Override
    public ResponseEntity<Boolean> unfollow(HttpServletRequest request, UnFollowingRequestDto requestDto) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return ResponseEntity.ok(followService.unfollow(email,requestDto));
    }

    @Override
    public ResponseEntity<Boolean> checkFollowing(HttpServletRequest httpServletRequest, String keyword) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(httpServletRequest).substring(7));
        return ResponseEntity.ok(followService.checkFollowing(email, keyword));
    }

    @Override
    public List<RecommendResponseDto> recommend(HttpServletRequest httpServletRequest) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(httpServletRequest).substring(7));
        return followService.recommend(email);
    }

    @Override
    public List<CelebRankingResponseDto> ranking() {
        return followService.ranking();
    }
}
