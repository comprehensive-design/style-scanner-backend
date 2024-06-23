package com.example.stylescanner.itemLike.repository;

import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.itemLike.entity.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Integer> {
    ItemLike findByItemIdAndUserId(Integer itemId, Long id);

    List<ItemLike> findAllByUserId(Long userId);

    @Query("SELECT il.item FROM ItemLike il GROUP BY il.item ORDER BY COUNT(il) DESC")
    List<Item> findAllItemsOrderByLikeCount();

    @Query("SELECT il.item.id, COUNT(il) FROM ItemLike il WHERE il.createdAt >= :startDate AND il.createdAt <= :endDate GROUP BY il.item.id")
    List<Object[]> findItemLikeCountsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
