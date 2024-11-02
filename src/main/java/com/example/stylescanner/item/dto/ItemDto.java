package com.example.stylescanner.item.dto;


import com.example.stylescanner.comment.dto.CommentDto;
import com.example.stylescanner.item.category.Category;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.itemLike.entity.ItemLike;
import com.example.stylescanner.post.dto.PostDto;
import com.example.stylescanner.post.entity.Post;
import com.example.stylescanner.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ItemDto {
    private Long id;

    private String feedUrl;

    private String name;

    private Integer price;

    private String itemUrl;

    private String shoppingLink;

    private Integer likeCount;

    private Category category;

    private String itemOption;

    private String brand;

    private String platform;

//    private boolean liked;

    public static ItemDto fromEntity(Item item) {
        List<ItemLike> likes = item.getLikes();
//        boolean isLiked = likes != null && likes.stream().anyMatch(like -> like.getUser().equals(user));
        return ItemDto.builder()
                .id(item.getId())
                .feedUrl(item.getFeedUrl())
                .name(item.getName())
                .price(item.getPrice())
                .category(item.getCategory())
                .itemOption(item.getItemOption())
                .brand(item.getBrand())
                .itemUrl(item.getItemUrl())
                .shoppingLink(item.getShoppingLink())
                .platform(item.getPlatform())
                .likeCount(likes != null ? likes.size() : 0)
//                .liked(isLiked)
                .build();
    }
}
