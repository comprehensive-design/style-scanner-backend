package com.example.stylescanner.notification.repository;

import com.example.stylescanner.notification.entity.Notification;
import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByReceiver(User user);
}
