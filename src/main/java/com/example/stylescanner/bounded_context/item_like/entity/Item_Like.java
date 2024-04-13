package com.example.stylescanner.bounded_context.item_like.entity;


import com.example.stylescanner.bounded_context.item.entity.Item;
import com.example.stylescanner.bounded_context.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Item_Like {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer like_id;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    @ManyToOne
    private Item item_id;

    @Column(nullable = false)
    @ManyToOne
    private User user_id;

}
