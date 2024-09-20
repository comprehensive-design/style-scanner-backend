package com.example.stylescanner.instagram.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.follow.service.FollowService;
import com.example.stylescanner.instagram.api.InstagramApi;
import com.example.stylescanner.instagram.dto.*;
import com.example.stylescanner.instagram.service.InstagramService;
import com.example.stylescanner.jwt.provider.JwtProvider;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class InstagramController implements InstagramApi {
    private final InstagramService instagramService;
    private final FollowService followService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Value("${cloud.aws.s3.bucket}")
    private String buketName;

    private final AmazonS3 amazonS3;

    @Override
    public CelebInstaResponseDto readCelebInsta(String username) {
        return instagramService.readCelebInsta(username);
    }


    @Override
    public HomeFeedDto getHomeFeed(HttpServletRequest request, @RequestParam int page, @RequestParam int size) throws IOException {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        Optional<User> user = userRepository.findByEmail(email);

        Page<Follow> paging = followService.followingListPaging(email,page,size);

        List<HomeFeedResponseDto> homeFeedResponseDtoList = instagramService.readHomeFeed(paging);
        HomeFeedDto homeFeedDto = new HomeFeedDto();

        homeFeedDto.setHomeFeedList(homeFeedResponseDtoList);
        homeFeedDto.setTotal_count(followService.getFollowCountByUser(user));
        return homeFeedDto;
    }

    @Override
    public FeedDto getFeed(FeedRequestDto feedRequestDto) {
        return instagramService.readFeed(feedRequestDto);
    }

    @Override
    public List<CarouselMediaDto> getCarouselMedia(String feed_code) throws IOException {
        return instagramService.findCarousel(feed_code);
    }

    @Override
    public String uploadSearchImg(MultipartFile searchImgFile) {

        String originalFilename = searchImgFile.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String uniqueFilename = "searchImage/"+UUID.randomUUID() + fileExtension;
        String fileUrl = "https://" + buketName + ".s3.amazonaws.com/" + uniqueFilename;

        try {
            amazonS3.putObject(new PutObjectRequest(buketName,uniqueFilename,searchImgFile.getInputStream(),null));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3");
        }

        System.out.println(fileUrl);
        return fileUrl;
    }

    @Override
    public ResponseEntity<byte[]> getInstagramImage(String imageUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // 서드파티 API에서 받은 이미지 URL로 요청
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);

            // 이미지 데이터를 클라이언트에 반환
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // 이미지 형식에 맞게 설정
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 예외 처리
        }
    }

    @Override
    public String getImageUrl(String feedCode) throws IOException {
        return instagramService.getImageUrl(feedCode);
    }
}
