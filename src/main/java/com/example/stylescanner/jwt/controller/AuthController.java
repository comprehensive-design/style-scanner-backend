package com.example.stylescanner.jwt.controller;


import com.example.stylescanner.jwt.api.AuthApi;
import com.example.stylescanner.jwt.dto.JwtDto;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final JwtProvider jwtProvider;

    @Override
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) {
        String encryptedRefreshToken = jwtProvider.resolveToken(request);

        if(!jwtProvider.validateToken(encryptedRefreshToken)) {
            return ResponseEntity.status(401).body("Refresh token is expired or invalid");
        }

        return ResponseEntity.ok(jwtProvider.reissueToken(encryptedRefreshToken.substring(7)));
    }


}
