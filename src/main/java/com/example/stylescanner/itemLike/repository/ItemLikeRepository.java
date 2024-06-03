package com.example.stylescanner.itemLike.repository;

import com.example.stylescanner.itemLike.entity.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Integer> {
    ItemLike findByItemIdAndUserId(Integer itemId, Long id);

    List<ItemLike> findAllByUserId(Long userId);
}
