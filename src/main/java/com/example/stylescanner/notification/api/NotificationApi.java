package com.example.stylescanner.notification.api;


import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.notification.dto.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/notification")
@Tag(name = "notification", description = "커뮤니티 알림 API")
public interface NotificationApi {
    @GetMapping("/me")
    @Operation(summary = "사용자 커뮤니티 알림 조회 메서드", description = "사용자의 커뮤니티 알림을 조회하기 위한 메서드입니다.")
    List<NotificationDto> me(HttpServletRequest request);

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "사용자 커뮤니티 알림 삭제 메서드", description = "사용자의 커뮤니티 알림을 삭제하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> delete(@PathVariable Integer notificationId);

    @GetMapping("/count")
    @Operation(summary = "읽지 않은 알림 개수 반환 메서드", description = "사용자가 읽지않은 알림 개수를 조회하기 위한 메서드입니다.")
    long count(HttpServletRequest request);
}
