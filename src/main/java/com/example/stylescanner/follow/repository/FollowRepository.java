package com.example.stylescanner.follow.repository;

import com.example.stylescanner.follow.entity.Follow;
import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUser(User user);
    Optional<Follow> findByUserAndFolloweeId(User user, String followeeId);
    boolean existsByFolloweeIdAndUser(String followeeId, User user);

    @Query("SELECT f.followeeId FROM Follow f WHERE f.user IN (SELECT f2.user FROM Follow f2 WHERE f2.followeeId IN :followeeIds AND f2.user <> :user)")
    List<String> findRecommendedFollowees(@Param("followeeIds") List<String> followeeIds, @Param("user") User user);

    @Query("SELECT f2.followeeId AS followeeId, COUNT(f2.followeeId) AS count " +
            "FROM Follow f1 " +
            "JOIN Follow f2 ON f1.user = f2.user " +
            "WHERE f1.followeeId IN :followeeIds AND f2.followeeId NOT IN :followeeIds " +
            "AND f1.user <> :user " +
            "GROUP BY f2.followeeId " +
            "ORDER BY count DESC")
    List<Map<String, Object>> findRecommendedFolloweesWithCount(@Param("followeeIds") List<String> followeeIds, @Param("user") User user);


    @Query("SELECT f.followeeId, COUNT(f.followeeId) AS followCount " +
            "FROM Follow f " +
            "GROUP BY f.followeeId " +
            "ORDER BY followCount DESC")
    List<Object[]> findTopFollowedCelebrities();
}
