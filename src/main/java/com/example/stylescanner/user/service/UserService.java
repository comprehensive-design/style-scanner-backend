package com.example.stylescanner.user.service;

import com.example.stylescanner.jwt.dto.JwtDto;
import com.example.stylescanner.jwt.provider.JwtProvider;
import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.dto.UserRegisterResponseDto;
import com.example.stylescanner.user.dto.UserSignRequestDto;
import com.example.stylescanner.user.dto.UserUpdateInfoDto;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
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

        //회원 중복 체크
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

    public Boolean update(String email, UserUpdateInfoDto requestDto) {
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
        if (requestDto.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(requestDto.getProfilePictureUrl());
        }
        if (requestDto.getBio() != null) {
            user.setBio(requestDto.getBio());
        }
        if (requestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        userRepository.save(user);

        return true;
    }

    public Boolean withdrawal(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
        return true;
    }

    public Boolean emailcheck(String email) {
        Optional<User> checkUserEmail = userRepository.findByEmail(email);
        if(checkUserEmail.isPresent()) {
            return true;
        }else{
            return false;
        }
    }
}
