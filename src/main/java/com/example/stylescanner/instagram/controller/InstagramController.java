package com.example.stylescanner.instagram.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.stylescanner.follow.dto.FollowingListResponseDto;
import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.follow.service.FollowService;
import com.example.stylescanner.instagram.api.InstagramApi;
import com.example.stylescanner.instagram.dto.CelebInstaResponseDto;
import com.example.stylescanner.instagram.dto.FeedDto;
import com.example.stylescanner.instagram.dto.FeedRequestDto;
import com.example.stylescanner.instagram.dto.HomeFeedResponseDto;
import com.example.stylescanner.instagram.service.InstagramService;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class InstagramController implements InstagramApi {
    private final InstagramService instagramService;
    private final FollowService followService;
    private final JwtProvider jwtProvider;


    @Value("${cloud.aws.s3.bucket}")
    private String buketName;

    private final AmazonS3 amazonS3;

    @Override
    public CelebInstaResponseDto readCelebInsta(String username) {
        return instagramService.readCelebInsta(username);
    }

//    @Override
//    public HomeFeedResponseDto getHomeFeed(HttpServletRequest request) {
//        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
//        FollowingListResponseDto followingList =  followService.followingList(email);
//        return instagramService.readHomeFeed(followingList);
//    }

    @Override
    public List<HomeFeedResponseDto> getHomeFeed(HttpServletRequest request, @RequestParam int page, @RequestParam int size) throws IOException {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));

        Page<Follow> paging = followService.followingListPaging(email,page,size);

        return instagramService.readHomeFeed(paging);
    }

    @Override
    public FeedDto getFeed(FeedRequestDto feedRequestDto) {
        return instagramService.readFeed(feedRequestDto);
    }

    @Override
    public List<String> getCarouselMedia(String feed_code) throws IOException {
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
}
