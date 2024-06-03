package com.example.stylescanner.notification.api;


import com.example.stylescanner.notification.dto.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/notification")
@Tag(name = "notification", description = "커뮤니티 알림 API")
public interface NotificationApi {
    @GetMapping("/me")
    @Operation(summary = "사용자 커뮤니티 알림 조회 메서드", description = "사용자의 커뮤니티 알림을 조회하기 위한 메서드입니다.")
    List<NotificationDto> me(HttpServletRequest request);
}
