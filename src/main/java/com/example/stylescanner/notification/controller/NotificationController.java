package com.example.stylescanner.notification.controller;

import com.example.stylescanner.jwt.provider.JwtProvider;
import com.example.stylescanner.notification.api.NotificationApi;
import com.example.stylescanner.notification.dto.NotificationDto;
import com.example.stylescanner.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;
    private final JwtProvider jwtProvider;


    @Override
    public List<NotificationDto> me(HttpServletRequest request) {
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return notificationService.me(currentUserEmail);
    }
}
