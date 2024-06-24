package com.example.stylescanner.item.entity;

import com.example.stylescanner.comment.entity.Comment;
import com.example.stylescanner.item.category.Category;
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

    @Column
    private String shoppingLink;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemLike> likes;

    @Enumerated(EnumType.STRING)
    @Column
    private Category category;

    @Column
    private String itemOption;

    @Column
    private String brand;

    @Builder
    public Item(String feedUrl, String name, Integer price, String itemUrl, Category category, String itemOption, String brand) {
        this.feedUrl = feedUrl;
        this.name = name;
        this.price = price;
        this.itemUrl = itemUrl;
        this.category = category;
        this.itemOption = itemOption;
        this.brand = brand;
    }
}
