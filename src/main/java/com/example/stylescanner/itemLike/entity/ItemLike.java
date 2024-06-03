package com.example.stylescanner.itemLike.entity;

import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "item_like")
public class ItemLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User user;

    @Builder
    public ItemLike(Item item, User user, LocalDateTime createdAt) {
        this.item = item;
        this.user = user;
        this.createdAt = createdAt;
    }
}
