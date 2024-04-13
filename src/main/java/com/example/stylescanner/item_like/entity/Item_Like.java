package com.example.stylescanner.item_like.entity;

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
public class Item_Like {
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
    public Item_Like(Item item, User user) {
        this.item = item;
        this.user = user;
    }
}
