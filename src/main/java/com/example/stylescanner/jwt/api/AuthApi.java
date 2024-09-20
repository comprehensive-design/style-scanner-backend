package com.example.stylescanner.jwt.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
@Tag(name = "auth", description = "토큰 발급/검증 관련 API")
public interface AuthApi {

    @PostMapping("/reissue")
    @Operation(summary = "헤더의 Authorization에 refreshToken을 넣고 호출하여 새로운 accessToken을 반환합니다. " , description = "이때 refreshToken도 만료되면 401을 반환합니다(이떄 로그인 창으로 유도하세요)")
    ResponseEntity<?> reissueAccessToken(HttpServletRequest request);


}
