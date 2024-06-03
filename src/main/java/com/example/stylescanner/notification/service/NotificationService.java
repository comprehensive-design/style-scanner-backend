package com.example.stylescanner.notification.service;


import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.notification.dto.NotificationDto;
import com.example.stylescanner.notification.entity.Notification;
import com.example.stylescanner.notification.repository.NotificationRepository;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationDto> me(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

        List<Notification> notifications = notificationRepository.findAllByReceiver(user);

        return notifications.stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());

    }
}
