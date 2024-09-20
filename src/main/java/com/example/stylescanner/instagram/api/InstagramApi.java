package com.example.stylescanner.instagram.api;

import com.example.stylescanner.instagram.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
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
    HomeFeedDto getHomeFeed(HttpServletRequest request,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "9") int size
                                    ) throws IOException;

    @GetMapping("/feed")
    @Operation(summary = "셀럽 계정 속 특정 피드 요청 메서드", description = "선택한 피드의 인덱스값과 미디어 아이디를 넘겨주면 해당 피드의 하위 피드를 가져옵니다.")
    FeedDto getFeed(@RequestBody FeedRequestDto feedRequestDto);

    @GetMapping("/getCarouselMedia")
    @Operation(summary = "feed_code 로 해당 피드의 하위 미디어들을 가져옵니다. ")
    List<CarouselMediaDto> getCarouselMedia(@RequestParam String feed_code) throws IOException;

    @PostMapping("/uploadSearchImg")
    @Operation(summary = "검색용 이미지 업로드 메서드", description = "사용자가 원하는 셀럽 계정이 검색되지 않거나 따로 이미지 검색이 필요할때 검색용 이미지를 서버에 등록하고 url을 반환합니다. ")
    String uploadSearchImg( @RequestPart(value="SearchImgFile")  MultipartFile searchImgFile);

    @GetMapping("/proxyImage")
    @Operation(summary = "인스타 이미지 URL을 이미지 파일(byte[])로 반환해줍니다.")
    ResponseEntity<byte[]> getInstagramImage(@RequestParam String imageUrl);

    @GetMapping("/getImageUrl")
    @Operation(summary = "feedCode로 해당 피드 이미지 url을 찾아서 반환합니다. ", description = "커뮤니티 게시글 작성시, 질문할 피드 이미지의 feedCode는 /home -> /getCarouselMedia 를 거쳐서 얻을수 있고 이를 params에 넣어 호출합니다.  ")
    String getImageUrl(@RequestParam String feedCode) throws IOException;
}
