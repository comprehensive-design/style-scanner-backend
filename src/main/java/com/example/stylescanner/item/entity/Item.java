package com.example.stylescanner.item.entity;

import com.example.stylescanner.comment.entity.Comment;
import com.example.stylescanner.itemLike.entity.ItemLike;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String feedUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String itemUrl;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemLike> likes;

    @Builder
    public Item(String feedUrl, String name, Integer price, String itemUrl) {
        this.feedUrl = feedUrl;
        this.name = name;
        this.price = price;
        this.itemUrl = itemUrl;
    }
}
