package com.example.stylescanner.instagram.api;

import com.example.stylescanner.instagram.dto.CelebInstaResponseDto;
import com.example.stylescanner.instagram.dto.FeedDto;
import com.example.stylescanner.instagram.dto.FeedRequestDto;
import com.example.stylescanner.instagram.dto.HomeFeedResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/insta")
@Tag(name="Insta", description = "인스타그램 정보 관련 API")
public interface InstagramApi {
    @GetMapping("/{username}")
    @Operation(summary="셀럽 인스타 요청 메서드", description = "셀럽의 인스타를 가져옵니다. (프로필 정보 + 피드 리스트)")
    CelebInstaResponseDto readCelebInsta(@PathVariable("username") String username);

    @GetMapping("/home")
    @Operation(summary="홈 화면 속 피드 리스트 요청 메서드", description = "사용자가 팔로잉 하고있는 셀럽의 가장 최근 피드를 가져옵니다.")
    HomeFeedResponseDto getHomeFeed(HttpServletRequest request);

    @GetMapping("/feed")
    @Operation(summary = "셀럽 계정 속 특정 피드 요청 메서드", description = "선택한 피드의 인덱스값과 미디어 아이디를 넘겨주면 해당 피드의 하위 피드를 가져옵니다.")
    FeedDto getFeed(@RequestBody FeedRequestDto feedRequestDto);
}
