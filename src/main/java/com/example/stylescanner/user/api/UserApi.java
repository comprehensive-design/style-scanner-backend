package com.example.stylescanner.user.api;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.jwt.dto.JwtDto;
import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.dto.UserRegisterResponseDto;
import com.example.stylescanner.user.dto.UserSignRequestDto;
import com.example.stylescanner.user.dto.UserUpdateInfoDto;
import com.example.stylescanner.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 관련 API")
public interface UserApi {
    @GetMapping("")
    @Operation(summary="사용자 목록 조회 메서드", description = "사용자 목록을 조회합니다.")
    List<User> list();

    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회 메서드", description = "사용자 상세 정보를 조회합니다.")
    User read(@RequestBody Long id);

    @PostMapping("/signup")
    @Operation(summary = "(회원가입)사용자 등록 메서드", description = "사용자의 가입 정보를 DB에 등록합니다.")
    ResponseEntity<Boolean> signup(@RequestBody UserRegisterRequestDto requestDto);

    @PostMapping("/login")
    @Operation(summary = "로그인 메서드", description = "사용자의 아이디, 패스워드를 받아 인증합니다. ")
    ResponseEntity<JwtDto> login(@RequestBody UserSignRequestDto requestDto);

    @GetMapping("/me")
    @Operation(summary = "사용자 본인 정보 조회 메서드", description = "사용자가 본인의 정보를 조회하기 위한 메서드입니다.")
    ResponseEntity<UserRegisterResponseDto> read(HttpServletRequest request);

    @PostMapping("/update")
    @Operation(summary = "사용자 정보 업데이트 메서드", description = "사용자의 수정된 정보를 받아 업데이트 합니다.")
    ResponseEntity<StateResponse> update(HttpServletRequest request, @RequestBody UserUpdateInfoDto requestDto);

    @PostMapping("/updateProfile")
    ResponseEntity<StateResponse> updateProfile(HttpServletRequest request, @RequestPart(value="profilePictureUrl")  MultipartFile profilePicture);

    @PostMapping("/withdrawal")
    @Operation(summary = "사용자 탈퇴 메서드", description = "사용자가 탈퇴하는 메서드입니다. ")
    ResponseEntity<StateResponse>  withdrawal(HttpServletRequest request);

    @GetMapping("/emailcheck")
    @Operation(summary="이메일 중복 확인 메서드", description = "이메일을 받아 존재하는지 확인하는 메서드입니다.")
    ResponseEntity<Boolean> emailcheck(@RequestParam(value = "email") String email);
}
