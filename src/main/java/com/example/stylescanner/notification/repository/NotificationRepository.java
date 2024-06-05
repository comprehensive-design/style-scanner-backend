package com.example.stylescanner.notification.repository;

import com.example.stylescanner.notification.entity.Notification;
import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByReceiver(User user);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.receiver = :receiver AND n.checked = false")
    long countByReceiverAndCheckedFalse(User receiver);
}
