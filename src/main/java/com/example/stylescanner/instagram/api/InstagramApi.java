package com.example.stylescanner.instagram.api;

import com.example.stylescanner.instagram.dto.CelebInstaResponseDto;
import com.example.stylescanner.instagram.dto.FeedDto;
import com.example.stylescanner.instagram.dto.FeedRequestDto;
import com.example.stylescanner.instagram.dto.HomeFeedResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/insta")
@Tag(name="Insta", description = "인스타그램 정보 관련 API")
public interface InstagramApi {
    @GetMapping("/{username}")
    @Operation(summary="셀럽 인스타 요청 메서드", description = "셀럽의 인스타를 가져옵니다. (프로필 정보 + 피드 리스트)")
    CelebInstaResponseDto readCelebInsta(@PathVariable("username") String username);

    @GetMapping("/home")
    @Operation(summary="홈 화면 속 피드 리스트 요청 메서드", description = "사용자가 팔로잉 하고있는 셀럽의 가장 최근 피드를 가져옵니다.")
    List<HomeFeedResponseDto> getHomeFeed(HttpServletRequest request,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size
                                    ) throws IOException;

    @GetMapping("/feed")
    @Operation(summary = "셀럽 계정 속 특정 피드 요청 메서드", description = "선택한 피드의 인덱스값과 미디어 아이디를 넘겨주면 해당 피드의 하위 피드를 가져옵니다.")
    FeedDto getFeed(@RequestBody FeedRequestDto feedRequestDto);

    @GetMapping("/getCarouselMedia")
    @Operation(summary = "feed_code 로 해당 피드의 하위 미디어들을 가져옵니다. ")
    List<String> getCarouselMedia(@RequestParam String feed_code) throws IOException;

    @PostMapping("/uploadSearchImg")
    @Operation(summary = "검색용 이미지 업로드 메서드", description = "사용자가 원하는 셀럽 계정이 검색되지 않거나 따로 이미지 검색이 필요할때 검색용 이미지를 서버에 등록하고 url을 반환합니다. ")
    String uploadSearchImg( @RequestPart(value="SearchImgFile")  MultipartFile searchImgFile);
}
