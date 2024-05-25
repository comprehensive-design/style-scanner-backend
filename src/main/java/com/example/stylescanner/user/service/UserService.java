package com.example.stylescanner.user.service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.jwt.dto.JwtDto;
import com.example.stylescanner.jwt.provider.JwtProvider;
import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.dto.UserRegisterResponseDto;
import com.example.stylescanner.user.dto.UserSignRequestDto;
import com.example.stylescanner.user.dto.UserUpdateInfoDto;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class UserService  {

    @Value("${cloud.aws.s3.bucket}")
    private String buketName;

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AmazonS3 amazonS3) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.amazonS3 = amazonS3;
    }

    public List<User> list(){
        return userRepository.findAll();
    }

    public User read(Long id){
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserRegisterResponseDto read(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserRegisterResponseDto(user);
    }


    public boolean signup(UserRegisterRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional <User> checkUserEmail = userRepository.findByEmail(email);
        if(checkUserEmail.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(password)
                .displayName(requestDto.getDisplayName())
                .birthdate(requestDto.getBirthdate())
                .gender(requestDto.getGender())
                .createdAt(LocalDateTime.now())
                .profilePictureUrl(null)
                .bio(null).build();

        userRepository.save(user);
        return true;
    }

    public JwtDto login(UserSignRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("잘못된 계정정보입니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        return JwtDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
                ;

    }

    public ResponseEntity<StateResponse> update(String email, UserUpdateInfoDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (requestDto.getDisplayName() != null) {
            user.setDisplayName(requestDto.getDisplayName());
        }
        if (requestDto.getGender() != null) {
            user.setGender(requestDto.getGender());
        }
        if (requestDto.getBirthdate() != null) {
            user.setBirthdate(requestDto.getBirthdate());
        }
        if (requestDto.getBio() != null) {
            user.setBio(requestDto.getBio());
        }
        if (requestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("정보를 성공적으로 업데이트했습니다.").build());
    }

    public ResponseEntity<StateResponse> withdrawal(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("성공적으로 회원탈퇴 처리되었습니다.").build());
    }

    public Boolean emailcheck(String email) {
        Optional<User> checkUserEmail = userRepository.findByEmail(email);
        if(checkUserEmail.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    private String saveProfilePicture(MultipartFile profilePicture)  {
        String originalFilename = profilePicture.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String uniqueFilename = UUID.randomUUID() + fileExtension;
        String fileUrl = "https://" + buketName + ".s3.amazonaws.com/" + uniqueFilename;

        try {
            amazonS3.putObject(new PutObjectRequest(buketName,uniqueFilename,profilePicture.getInputStream(),null));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3");
        }

        return fileUrl;
    }

    public ResponseEntity<StateResponse> updateProfile(String email, MultipartFile profilePicture) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        String profilePictureUrl = saveProfilePicture(profilePicture);

        String previousProfile = user.getProfilePictureUrl();
        user.setProfilePictureUrl(profilePictureUrl);

        //이전 프로필 이미지는 삭제
        if(!previousProfile.equals("null")){
            String filename = previousProfile.substring(previousProfile.lastIndexOf("/") + 1);
            // Check if the object exists before attempting to delete it
            if (amazonS3.doesObjectExist(buketName, filename)) {
                try {
                    amazonS3.deleteObject(new DeleteObjectRequest(buketName, filename));
                } catch (Exception e) {
                    user.setProfilePictureUrl(previousProfile);
                    throw new RuntimeException("Failed to delete file from S3", e);
                }
            }
        }

        userRepository.save(user);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("프로필 이미지를 성공적으로 등록했습니다.").build());
    }
}
