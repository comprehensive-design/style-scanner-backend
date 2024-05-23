package com.example.stylescanner.follow.repository;

import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUser(User user);
    Optional<Follow> findByUserAndFolloweeId(User user, String followeeId);
    boolean existsByFolloweeIdAndUser(String followeeId, User user);
}
