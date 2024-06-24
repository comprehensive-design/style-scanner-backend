package com.example.stylescanner.item.repository;

import com.example.stylescanner.item.category.Category;
import com.example.stylescanner.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>{
    List<Item> findByCategory(Category category);
//    List<Item> findByCategoryIn(List<Category> categories);

    @Query("SELECT i FROM Item i LEFT JOIN ItemLike il ON i = il.item WHERE i.category IN :categories GROUP BY i.id ORDER BY COUNT(il) DESC")
    List<Item> findByCategoryInOrderByLikeCount(@Param("categories") List<Category> categories);

}
