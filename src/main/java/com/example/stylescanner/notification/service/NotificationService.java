package com.example.stylescanner.notification.service;


import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.notification.dto.NotificationDto;
import com.example.stylescanner.notification.entity.Notification;
import com.example.stylescanner.notification.repository.NotificationRepository;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<NotificationDto> me(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

        List<Notification> notifications = notificationRepository.findAllByReceiver(user);

        List<NotificationDto> notificationDtos = notifications.stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());

        notifications.forEach(notification -> notification.setChecked(true));
        notificationRepository.saveAll(notifications);

        return notificationDtos;

    }

    @Transactional
    public ResponseEntity<StateResponse> delete(Integer notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("not found notification"));

        notificationRepository.delete(notification);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("알림이 성공적으로 삭제되었습니다.").build());
    }

    public long getUnreadNotificationCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));
        return notificationRepository.countByReceiverAndCheckedFalse(user);
    }
}
