package com.example.stylescanner.item_like.entity;

import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor

public class Item_Like {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer like_id;

    @CreatedDate
    private LocalDateTime created_at;

    @ManyToOne
    private Item item_id;

    @ManyToOne
    private User user_id;

}
