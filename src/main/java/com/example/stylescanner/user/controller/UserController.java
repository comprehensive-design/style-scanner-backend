package com.example.stylescanner.user.controller;

import com.example.stylescanner.user.api.UserApi;
import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.dto.UserRegisterResponseDto;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.service.UserService;
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

    @Override
    public List<User> list() {
        return userService.list();
    }

    @Override
    public User read(Long id) {
        return userService.read(id);
    }

    @Override
    public User signup(UserRegisterRequestDto requestDto) {
        userService.signup(requestDto);
        return requestDto.toEntity();
    }


}
