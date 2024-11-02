package com.example.stylescanner.item.dto;

import lombok.Data;

@Data
public class ItemCreateDto {
    private String title;
    private String imageUrl;
    private int cost;
    private String shoppingLink;
    private String sellerIcon;
    private String sellerName;
    private String category;
}
