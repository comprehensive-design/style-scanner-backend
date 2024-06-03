package com.example.stylescanner.itemLike.service;


import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.repository.ItemRepository;
import com.example.stylescanner.itemLike.entity.ItemLike;
import com.example.stylescanner.itemLike.repository.ItemLikeRepository;
import com.example.stylescanner.post.entity.Post;
import com.example.stylescanner.post.repository.PostRepository;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemLikeService {
    private final ItemRepository itemRepository;
    private final ItemLikeRepository itemLikeRepository;
    private final UserRepository userRepository;

    public Boolean liked(Integer itemId, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

        ItemLike userLiked = itemLikeRepository.findByItemIdAndUserId(itemId, user.getId());

        return userLiked != null;
    }

    public List<ItemDto> me(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

        List<ItemLike> itemLikes = itemLikeRepository.findAllByUserId(user.getId());

        return itemLikes.stream()
                .map(itemLike -> {
                    Item item = itemLike.getItem();
                    return ItemDto.fromEntity(item);
                })
                .collect(Collectors.toList());
    }

    public ResponseEntity<StateResponse> create(Integer itemId, String currentUserEmail){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("not found item"));
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

        ItemLike userLiked = itemLikeRepository.findByItemIdAndUserId(itemId, user.getId());

        if (userLiked != null) {
            return ResponseEntity.badRequest().body(StateResponse.builder().code("FAILURE").message("이미 좋아요를 누르셨습니다.").build());
        }

        ItemLike itemLike = ItemLike.builder()
                .createdAt(LocalDateTime.now())
                .user(user)
                .item(item)
                .build();

        itemLikeRepository.save(itemLike);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("좋아요가 완료 되었습니다.").build());

    }

    public ResponseEntity<StateResponse> delete(Integer itemId, String currentUserEmail){
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));
        ItemLike itemLike = itemLikeRepository.findByItemIdAndUserId(itemId, user.getId());

        itemLikeRepository.delete(itemLike);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("좋아요가 취소 되었습니다.").build());
    }


}
