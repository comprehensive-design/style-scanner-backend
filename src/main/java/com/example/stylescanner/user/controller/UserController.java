package com.example.stylescanner.user.controller;

import com.example.stylescanner.jwt.dto.JwtDto;
import com.example.stylescanner.jwt.provider.JwtProvider;
import com.example.stylescanner.user.api.UserApi;
import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.dto.UserSignRequestDto;
import com.example.stylescanner.user.dto.UserUpdateInfoDto;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApi {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Override
    public List<User> list() {
        return userService.list();
    }

    @Override
    public User read(Long id) {
        return userService.read(id);
    }

    @Override
    public ResponseEntity<Boolean> signup(UserRegisterRequestDto requestDto) {
        return new ResponseEntity<>(userService.signup(requestDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JwtDto> login(UserSignRequestDto requestDto) {
        return new ResponseEntity<>(userService.login(requestDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> read(HttpServletRequest request) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return ResponseEntity.ok(userService.read(email));
    }

    @Override
    public ResponseEntity<Boolean> update(HttpServletRequest request, UserUpdateInfoDto requestDto) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return ResponseEntity.ok(userService.update(email,requestDto));
    }

    @Override
    public ResponseEntity<Boolean> withdrawal(HttpServletRequest request) {
        String email = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return ResponseEntity.ok(userService.withdrawal(email));
    }

    @Override
    public ResponseEntity<Boolean> emailcheck(String email) {
        return ResponseEntity.ok(userService.emailcheck(email));
    }


}
