package com.example.stylescanner.notification.dto;


import com.example.stylescanner.notification.entity.Notification;
import com.example.stylescanner.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private LocalDateTime createdAt;
    private boolean checked;
    private Long postId;
    private String postContent;
    private String commentContent;

    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .createdAt(notification.getCreatedAt())
                .checked(notification.isChecked())
                .postId(notification.getComment().getPost().getId())
                .postContent(notification.getComment().getPost().getContent())
                .commentContent(notification.getComment().getContent())
                .build();
    }

}
