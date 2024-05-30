package com.example.stylescanner.follow.api;

import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.follow.dto.FollowingRequestDto;
import com.example.stylescanner.follow.dto.RecommendResponseDto;
import com.example.stylescanner.follow.dto.UnFollowingRequestDto;
import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/follow")
@Tag(name = "Follow", description = "팔로우 API")
public interface FollowApi {
    @GetMapping("")
    @Operation(summary = "팔로잉 목록 조회 메서드", description = "모든 팔로잉 데이터 목록을 보여주는 메서드입니다.")
    List<Follow> list();

    @GetMapping("/{id}")
    @Operation(summary = "팔로잉 상세 조회 메서드", description = "해당 팔로잉 데이터를 보여주는 메서드입니다. 없으면 IllegalException")
    ResponseEntity<Follow> read(@PathVariable Long id);

    @GetMapping("/search")
    @Operation(summary = "셀럽 검색 메서드", description = "셀럽 계정을 검색하여 검색 결과를 보여주는 메서드입니다.")
    ResponseEntity<CelebProfileResponseDto> search(@RequestParam(value="keyword") String keyword);

    @PostMapping("/following")
    @Operation(summary = "팔로잉 메서드", description = "사용자의 토큰값과 팔로잉 셀럽 followeeId을 넘겨주면 해당 셀럽의 팔로잉 정보를 DB에 저장합니다.")
    ResponseEntity<Boolean> follow(HttpServletRequest httpServletRequest, @RequestBody FollowingRequestDto requestDto);

    @GetMapping("/followingList")
    @Operation(summary = "사용자 팔로잉 목록 메서드", description = "현재 사용자의 팔로잉 목록을 반환하는 메서드입니다.")
    ResponseEntity<FollowingListResponseDto> followingList(HttpServletRequest httpServletRequest);

    @PostMapping("/unfollowing")
    @Operation(summary = "사용자 언팔로잉 메서드", description = "언팔로잉 하는 셀럽 followeeId를 넘겨주면 해당 셀럽의 팔로잉을 정보를 제거합니다.")
    ResponseEntity<Boolean> unfollow(HttpServletRequest request, @RequestBody UnFollowingRequestDto requestDto);

    @GetMapping("/checkFollowing")
    @Operation(summary = "팔로잉 체크 메서드", description = "셀럽의 아이디값을 넘겨주면 해당 셀럽의 팔로잉 유무를 반환합니다")
    ResponseEntity<Boolean> checkFollowing(HttpServletRequest httpServletRequest, @RequestParam(value="keyword") String keyword);

    @GetMapping("/recommend")
    @Operation(summary = "셀럽 추천 메서드", description = "현재 사용자의 팔로잉 목록을 바탕으로 다른 셀럽 계정을 추천합니다.")
    List<RecommendResponseDto> recommend(HttpServletRequest httpServletRequest);

}
