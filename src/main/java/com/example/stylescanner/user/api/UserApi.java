package com.example.stylescanner.user.api;

import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.dto.UserRegisterResponseDto;
import com.example.stylescanner.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    User signup(@RequestBody UserRegisterRequestDto requestDto);

}
